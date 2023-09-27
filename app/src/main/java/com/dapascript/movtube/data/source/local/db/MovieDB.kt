package com.dapascript.movtube.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dapascript.movtube.data.source.local.dao.MovieDao
import com.dapascript.movtube.data.source.local.dao.MovieKeysDao
import com.dapascript.movtube.data.source.local.model.MovieEntity
import com.dapascript.movtube.data.source.local.model.MovieKeys

@Database(
    entities = [MovieEntity::class, MovieKeys::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDB : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun movieKeysDao(): MovieKeysDao
}