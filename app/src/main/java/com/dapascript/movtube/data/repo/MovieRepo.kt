package com.dapascript.movtube.data.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dapascript.movtube.data.source.MoviesMediator
import com.dapascript.movtube.data.source.local.db.MovieDB
import com.dapascript.movtube.data.source.local.model.MovieEntity
import com.dapascript.movtube.data.source.remote.model.DetailResponse
import com.dapascript.movtube.data.source.remote.model.VideoResponse
import com.dapascript.movtube.data.source.remote.service.ApiService
import com.dapascript.movtube.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val movieDB: MovieDB,
    private val coroutineDispatcher: CoroutineDispatcher
) : MovieRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getMovies(): Flow<PagingData<MovieEntity>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            initialLoadSize = 10,
            prefetchDistance = 1,
            enablePlaceholders = false
        ),
        remoteMediator = MoviesMediator(apiService, movieDB),
        pagingSourceFactory = { movieDB.movieDao().getMovies() }
    ).flow

    override fun getDetailMovies(id: Int): Flow<Resource<DetailResponse>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.getDetailMovie(id)
            emit(Resource.Success(response))
        } catch (e: Throwable) {
            emit(Resource.Error(e))
        }
    }.flowOn(coroutineDispatcher)

    override fun getVideo(id: Int): Flow<Resource<VideoResponse>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.getVideo(id)
            emit(Resource.Success(response))
        } catch (e: Throwable) {
            emit(Resource.Error(e))
        }
    }.flowOn(coroutineDispatcher)
}

interface MovieRepository {
    fun getMovies(): Flow<PagingData<MovieEntity>>
    fun getDetailMovies(id: Int): Flow<Resource<DetailResponse>>
    fun getVideo(id: Int): Flow<Resource<VideoResponse>>
}