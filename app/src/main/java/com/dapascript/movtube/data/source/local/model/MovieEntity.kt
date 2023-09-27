package com.dapascript.movtube.data.source.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    val id: Int,
    val overview: String?,
    val title: String?,
    val posterPath: String?,
    val date: String?,
)
