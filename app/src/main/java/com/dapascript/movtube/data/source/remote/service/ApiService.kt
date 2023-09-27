package com.dapascript.movtube.data.source.remote.service

import com.dapascript.movtube.data.source.remote.model.DetailResponse
import com.dapascript.movtube.data.source.remote.model.MovieResponse
import com.dapascript.movtube.data.source.remote.model.VideoResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    companion object {
        private const val apiKey = "4f948dc2d121184b17586b04a38b778a"
    }

    // list movie
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = Companion.apiKey,
    ): MovieResponse

    // detail movie
    @GET("movie/{movie_id}")
    suspend fun getDetailMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = Companion.apiKey,
    ): DetailResponse

    // video movie
    @GET("movie/{movie_id}/videos")
    suspend fun getVideo(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = Companion.apiKey,
    ): VideoResponse

    // search movie
    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = Companion.apiKey,
    ): MovieResponse
}