package com.example.pocketlibrary.discovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketlibrary.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class DiscoverState(
    val loading: Boolean = true,
    val items: List<Book> = emptyList(),
    val page: Int = 0,
    val error: String? = null
)

class DiscoverViewModel(
    private val repo: DiscoverRepository = DiscoverRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(DiscoverState())
    val state: StateFlow<DiscoverState> = _state

    init { reload() }

    fun reload() {
        _state.value = _state.value.copy(loading = true, error = null)
        viewModelScope.launch {
            runCatching { repo.load(_state.value.page) }
                .onSuccess { _state.value = _state.value.copy(loading = false, items = it) }
                .onFailure { e -> _state.value = _state.value.copy(loading = false, error = e.message) }
        }
    }

    fun nextTopic() {
        _state.value = _state.value.copy(page = _state.value.page + 1)
        reload()
    }
}
