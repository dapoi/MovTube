package com.dapascript.movtube.utils

import com.dapascript.movtube.data.source.remote.model.ResultsItem

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val throwable: Throwable) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}
