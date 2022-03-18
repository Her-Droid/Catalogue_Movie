package com.freisia.cataloguemoviedb.data.source

import com.freisia.cataloguemoviedb.data.source.network.ApiResponse
import com.freisia.cataloguemoviedb.data.source.network.ApiService
import com.freisia.cataloguemoviedb.data.source.response.*
import com.freisia.cataloguemoviedb.utils.EspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun getMovieList(
        page:Int,
        language: String = "en-US",
        sortBy: String = "popularity.desc",
        includeAdult: Boolean = false,
        genres: String? = null
    ): Flow<ApiResponse<DataResponse<ResultMoviesResponse>>>{
        return flow{
            val response = apiService.getMovie(
                page,language,sortBy,includeAdult, genres
            )
            if (response.code() == 200) {
                // parse body
                val result = response.body()
                // check response body
                if (result != null) {
                    emit(ApiResponse.Success(result))
                } else {
                    emit(ApiResponse.Empty)
                }
            } else {
                // in case of invalid api key or something else
                response.errorBody()?.charStream()?.let {
                    val errorMessage = JSONObject(it.readText()).getString("status_message")
                    // emit error message
                    emit(ApiResponse.Error(errorMessage))
                }
            }
        }.catch { e ->
            emit(ApiResponse.Error(e.message.toString()))
        }.flowOn(dispatcher)
    }

    suspend fun getMovieById(id: Int): Flow<ApiResponse<MovieResponse>> {
        return flow {
            // fetch movie by id
            val apiResponse = apiService.getMovieById(id)
            // check for response validation
            if (apiResponse.code() == 200) {
                // parse body
                val result = apiResponse.body()
                // check response body
                if (result != null) {
                    emit(ApiResponse.Success(result))
                } else {
                    emit(ApiResponse.Empty)
                }
            } else {
                // in case of invalid api key or something else
                apiResponse.errorBody()?.charStream()?.let {
                    val errorMessage = JSONObject(it.readText()).getString("status_message")
                    // emit error message
                    emit(ApiResponse.Error(errorMessage))
                }
            }
        }.catch { e ->
            // emit exception
            emit(ApiResponse.Error(e.message.toString()))
        }.flowOn(dispatcher)
    }

    suspend fun getListGenres(language: String = "en-US"): Flow<ApiResponse<DataGenresResponse>> {
        return flow {
            EspressoIdlingResource.increment()
            // fetch movie by id
            val apiResponse = apiService.getListGenre(language)
            // check for response validation
            if (apiResponse.code() == 200) {
                // parse body
                val result = apiResponse.body()
                // check response body
                if (result != null) {
                    emit(ApiResponse.Success(result))
                } else {
                    emit(ApiResponse.Empty)
                }
            } else {
                // in case of invalid api key or something else
                apiResponse.errorBody()?.charStream()?.let {
                    val errorMessage = JSONObject(it.readText()).getString("status_message")
                    // emit error message
                    emit(ApiResponse.Error(errorMessage))
                }
            }
        }.catch { e ->
            // emit exception
            if(!EspressoIdlingResource.getEspressoIdlingResourceForMainActivity().isIdleNow){
                EspressoIdlingResource.decrement()
            }
            emit(ApiResponse.Error(e.message.toString()))
        }.flowOn(dispatcher)
    }

    suspend fun getReviewById(
        id: Int,
        page: Int,
        language: String = "en-US"
    ): Flow<ApiResponse<DataResponse<ResultReviewResponse>>> {
        return flow {
            // fetch movie by id
            val apiResponse = apiService.getReviews(id,page,language)
            // check for response validation
            if (apiResponse.code() == 200) {
                // parse body
                val result = apiResponse.body()
                // check response body
                if (result != null) {
                    emit(ApiResponse.Success(result))
                } else {
                    emit(ApiResponse.Empty)
                }
            } else {
                // in case of invalid api key or something else
                apiResponse.errorBody()?.charStream()?.let {
                    val errorMessage = JSONObject(it.readText()).getString("status_message")
                    // emit error message
                    emit(ApiResponse.Error(errorMessage))
                }
            }
        }.catch { e ->
            // emit exception
            emit(ApiResponse.Error(e.message.toString()))
        }.flowOn(dispatcher)
    }

    suspend fun getTrailerById(
        id: Int,
        language: String = "en-US"
    ): Flow<ApiResponse<VideoResponse>>{
        return flow {
            val apiResponse = apiService.getTrailer(id, language)
            if (apiResponse.code() == 200) {
                // parse body
                val result = apiResponse.body()
                // check response body
                if (result != null) {
                    emit(ApiResponse.Success(result))
                } else {
                    emit(ApiResponse.Empty)
                }
            } else {
                // in case of invalid api key or something else
                apiResponse.errorBody()?.charStream()?.let {
                    val errorMessage = JSONObject(it.readText()).getString("status_message")
                    // emit error message
                    emit(ApiResponse.Error(errorMessage))
                }
            }
        }.catch { e ->
            // emit exception
            emit(ApiResponse.Error(e.message.toString()))
        }.flowOn(dispatcher)
    }
}