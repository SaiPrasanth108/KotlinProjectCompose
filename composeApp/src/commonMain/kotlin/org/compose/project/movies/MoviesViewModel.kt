package org.compose.project.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.compose.project.data.models.movies.MovieResult
import org.compose.project.data.models.movies.Movies
import org.compose.project.data.network.KtorClient
import org.compose.project.data.network.Repository
import org.compose.project.data.network.Response

class MoviesViewModel(
    private val repository: Repository = KtorClient.repository
): ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.fetchMovies().collect { result ->

                when(result) {

                    is Response.Loading -> {
                        _homeState.update {
                            it.copy(
                                isLoading = true,
                                error = null
                            )
                        }
                    }

                    is Response.Success -> {
                        _homeState.update {
                            it.copy(
                                isLoading = false,
                                error = null,
                                movies = result.data.results
                            )
                        }
                    }

                    is Response.Error -> {
                        _homeState.update {
                            it.copy(
                                isLoading = false,
                                error = result.error.message,
                            )
                        }
                    }
                }


            }
        }
    }
}

data class HomeState(
    val movies: List<MovieResult> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

