package org.compose.project.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.compose.project.data.models.details.MovieDetails
import org.compose.project.data.models.movies.MovieResult
import org.compose.project.data.network.KtorClient
import org.compose.project.data.network.Repository
import org.compose.project.data.network.Response

class DetailViewModel(
    private val repository: Repository = KtorClient.repository
): ViewModel() {

    private val _detailState = MutableStateFlow(DetailState())
    val detailState = _detailState.asStateFlow()

    fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            repository.fetchMovieDetails(movieId).collect { result ->

                when(result) {

                    is Response.Loading -> {
                        _detailState.update {
                            it.copy(
                                isLoading = true,
                                error = null
                            )
                        }
                    }

                    is Response.Success -> {
                        _detailState.update {
                            it.copy(
                                isLoading = false,
                                error = null,
                                movieDetails = result.data
                            )
                        }
                    }

                    is Response.Error -> {
                        _detailState.update {
                            it.copy(
                                isLoading = false,
                                error = result.error?.message
                            )
                        }
                    }

                }
            }
        }
    }
}

data class DetailState(
    val movieDetails: MovieDetails? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)