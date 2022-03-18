package com.freisia.cataloguemoviedb.data.source.network

import androidx.annotation.Nullable
import com.freisia.cataloguemoviedb.data.source.response.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("discover/movie")
    suspend fun getMovie(
        @Query("page") page: Int,
        @Query("language") language: String,
        @Query("sort_by") popularity: String,
        @Query("include_adult") includeAdult: Boolean,
        @Nullable @Query("with_genres") genres: String?
    ) : Response<DataResponse<ResultMoviesResponse>>

    @GET("movie/{id}")
    suspend fun getMovieById(
        @Path("id") id: Int
    ): Response<MovieResponse>

    @GET("genre/movie/list")
    suspend fun getListGenre(
        @Query("language") language: String,
    ): Response<DataGenresResponse>

    @GET("movie/{id}/reviews")
    suspend fun getReviews(
        @Path("id") id: Int,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<DataResponse<ResultReviewResponse>>

    @GET("movie/{id}/videos")
    suspend fun getTrailer(
        @Path("id") id: Int,
        @Query("language") language: String
    ): Response<VideoResponse>
}
