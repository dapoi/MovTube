package com.dapascript.movtube.data.source.remote.model

import com.squareup.moshi.Json

data class MovieResponse(

	@Json(name="dates")
	val dates: Dates? = null,

	@Json(name="page")
	val page: Int? = null,

	@Json(name="total_pages")
	val total_pages: Int? = null,

	@Json(name="results")
	val results: List<ResultsItem?>? = null,

	@Json(name="total_results")
	val total_results: Int? = null
)

data class Dates(

	@Json(name="maximum")
	val maximum: String? = null,

	@Json(name="minimum")
	val minimum: String? = null
)

data class ResultsItem(

	@Json(name="overview")
	val overview: String? = null,

	@Json(name="original_language")
	val original_language: String? = null,

	@Json(name="original_title")
	val original_title: String? = null,

	@Json(name="video")
	val video: Boolean? = null,

	@Json(name="title")
	val title: String? = null,

	@Json(name="genre_ids")
	val genre_ids: List<Int?>? = null,

	@Json(name="poster_path")
	val poster_path: String? = null,

	@Json(name="backdrop_path")
	val backdrop_path: String? = null,

	@Json(name="release_date")
	val release_date: String? = null,

	@Json(name="popularity")
	val popularity: Any? = null,

	@Json(name="vote_average")
	val vote_average: Any? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="adult")
	val adult: Boolean? = null,

	@Json(name="vote_count")
	val vote_count: Int? = null
)
