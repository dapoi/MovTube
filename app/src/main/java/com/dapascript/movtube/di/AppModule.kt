package com.dapascript.movtube.di

import android.content.Context
import androidx.room.Room
import com.dapascript.movtube.data.repo.MovieRepository
import com.dapascript.movtube.data.repo.MovieRepositoryImpl
import com.dapascript.movtube.data.source.local.db.MovieDB
import com.dapascript.movtube.data.source.remote.service.ApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    @Provides
    fun provideApiService(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun provideMovieDB(@ApplicationContext context: Context): MovieDB {
        return Room.databaseBuilder(
            context,
            MovieDB::class.java,
            "movie.db"
        ).build()
    }

    @Provides
    fun provideMovieDao(movieDB: MovieDB) = movieDB.movieDao()

    @Provides
    fun provideRemoteKeyDao(movieDB: MovieDB) = movieDB.movieKeysDao()

    @Provides
    fun provideMovieRepository(
        apiService: ApiService,
        movieDB: MovieDB,
        coroutineDispatcher: CoroutineDispatcher
    ): MovieRepository {
        return MovieRepositoryImpl(apiService, movieDB, coroutineDispatcher)
    }

    @Provides
    fun provideIOCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO
}