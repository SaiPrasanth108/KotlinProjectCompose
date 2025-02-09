package org.compose.project.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.compose.project.data.models.details.MovieDetails
import org.compose.project.data.models.movies.Movies

class Repository(
    private val client: HttpClient
) {

    fun fetchMovies(type: String = "popular"): Flow<Response<Movies>> = flow {

        emit(Response.Loading())

        val movieDto = client.get {
            url {
                protocol = URLProtocol.HTTPS
                host = "api.themoviedb.org"
                encodedPath = "/3/movie/$type"
                parameters.append("api_key", "faa84039c1876a6d0f9856208f4b03ef")
            }
        }.body<Movies>()

        emit(Response.Success(movieDto))

    }.catch { error ->
        emit(Response.Error(error))
    }

    fun fetchMovieDetails(id: Int): Flow<Response<MovieDetails>> = flow {

        emit(Response.Loading())

        val movieDetails = client.get {
            url {
                protocol = URLProtocol.HTTPS
                host = "api.themoviedb.org"
                encodedPath = "/3/movie/$id"
                parameters.append("api_key", "faa84039c1876a6d0f9856208f4b03ef")
            }
        }.body<MovieDetails>()

        emit(Response.Success(movieDetails))

    }.catch { error ->
        emit(Response.Error(error))
    }
}