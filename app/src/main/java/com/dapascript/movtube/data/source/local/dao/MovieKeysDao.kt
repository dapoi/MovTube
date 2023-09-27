package com.dapascript.movtube.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dapascript.movtube.data.source.local.model.MovieKeys

@Dao
interface MovieKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieKeys(movieKeys: List<MovieKeys>)

    @Query("SELECT * FROM movies_keys WHERE movieId = :movieId")
    suspend fun getMovieKeys(movieId: Int): MovieKeys?

    @Query("DELETE FROM movies_keys")
    suspend fun clearMovieKeys()
}