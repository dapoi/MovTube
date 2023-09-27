package com.dapascript.movtube.data.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.dapascript.movtube.data.source.local.db.MovieDB
import com.dapascript.movtube.data.source.local.model.MovieEntity
import com.dapascript.movtube.data.source.local.model.MovieKeys
import com.dapascript.movtube.data.source.remote.model.ResultsItem
import com.dapascript.movtube.data.source.remote.service.ApiService
import okio.IOException
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class MoviesMediator(
    private val apiService: ApiService,
    private val movieDB: MovieDB
) : RemoteMediator<Int, MovieEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with endOfPaginationReached = false because Paging
                // will call this method again if TopTVRemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val response = apiService.getNowPlayingMovies(page).results
            val endOfPagination = response.isNullOrEmpty()

            movieDB.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    movieDB.movieKeysDao().clearMovieKeys()
                    movieDB.movieDao().clearMovies()
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPagination) null else page + 1
                val movieEntity = response?.map { movieEntityMapper(it!!) }
                val keys = movieEntity?.map {
                    MovieKeys(
                        movieId = it.id,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                movieDB.movieKeysDao().insertMovieKeys(keys!!)
                movieDB.movieDao().insertMovies(movieEntity)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (ioException: IOException) {
            return MediatorResult.Error(ioException)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private fun movieEntityMapper(response: ResultsItem): MovieEntity {
        return MovieEntity(
            id = response.id!!,
            overview = response.overview,
            title = response.title,
            posterPath = response.poster_path,
            date = response.release_date
        )
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieEntity>): MovieKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            movieDB.movieKeysDao().getMovieKeys(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieEntity>): MovieKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            movieDB.movieKeysDao().getMovieKeys(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, MovieEntity>): MovieKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                movieDB.movieKeysDao().getMovieKeys(id)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}