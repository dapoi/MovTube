package com.dapascript.movtube.data.source.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dapascript.movtube.data.source.local.model.MovieEntity
import com.dapascript.movtube.data.source.local.model.MovieFavEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    /**
     * paging
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Query("SELECT * FROM movies")
    fun getMovies(): PagingSource<Int, MovieEntity>

    @Query("DELETE FROM movies")
    suspend fun clearMovies()

    /**
     * favorite
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(movieFav: MovieFavEntity)

    @Delete
    suspend fun deleteFavorite(movieFav: MovieFavEntity)

    @Query("SELECT * FROM favorite")
    fun getFavorite(): Flow<List<MovieFavEntity>>

    @Query("SELECT EXISTS (SELECT 1 FROM favorite WHERE id = :id)")
    fun isFavorite(id: Int): Flow<Boolean>
}