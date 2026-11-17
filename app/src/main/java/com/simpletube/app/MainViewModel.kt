package com.simpletube.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simpletube.app.data.NewPipeClient
import com.simpletube.app.data.VideoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val loading: Boolean = false,
    val results: List<VideoItem> = emptyList(),
    val error: String? = null,
    val searched: Boolean = false
)

class MainViewModel : ViewModel() {

    private val _state = MutableStateFlow(SearchUiState())
    val state: StateFlow<SearchUiState> = _state.asStateFlow()

    fun onQueryChange(value: String) {
        _state.value = _state.value.copy(query = value)
    }

    fun search() {
        val query = _state.value.query.trim()
        if (query.isEmpty() || _state.value.loading) return

        _state.value = _state.value.copy(loading = true, error = null, searched = true)

        viewModelScope.launch {
            runCatching { NewPipeClient.search(query) }
                .onSuccess { result ->
                    _state.value = _state.value.copy(loading = false, results = result)
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        loading = false,
                        results = emptyList(),
                        error = error.message ?: "Error de red"
                    )
                }
        }
    }
}
