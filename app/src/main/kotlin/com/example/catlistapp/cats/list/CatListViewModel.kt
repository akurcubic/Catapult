package com.example.catlistapp.cats.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catlistapp.cats.api.model.CatApiModel
import com.example.catlistapp.cats.list.model.CatListUiModel
import com.example.catlistapp.cats.repository.CatsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

class CatListViewModel (
    private val repository: CatsRepository = CatsRepository,
) : ViewModel(){

    private val _state = MutableStateFlow(CatListContract.CatListState())
    val state = _state.asStateFlow()
    private fun setState(reducer: CatListContract.CatListState.() -> CatListContract.CatListState) = _state.update(reducer)

    private val events = MutableSharedFlow<CatListContract.CatListUiEvent>()

    fun setEvent(event: CatListContract.CatListUiEvent) = viewModelScope.launch { events.emit(event) }

    init {
        observeEvents()
        fetchAllCats()
        observeSearchQuery()
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            events
                .filterIsInstance<CatListContract.CatListUiEvent.SearchQueryChanged>()
                .debounce(2.seconds)
                .collect {}
        }
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when (it) {

                    is CatListContract.CatListUiEvent.CloseSearchMode -> setState { copy(isSearchMode = false) }
                    is CatListContract.CatListUiEvent.SearchQueryChanged -> {
                        setState {
                            copy(query = it.query, isSearchMode = true)
                        }
                        filterCats(it.query)

                    }
                }
            }
        }
    }

    private fun filterCats(query: String) {
        viewModelScope.launch {
            val filteredCats = if (query.isEmpty()) {
                state.value.cats
            } else {
                state.value.cats.filter {
                    it.name.contains(query, ignoreCase = true)
                }
            }
            setState {
                copy(filteredCats = filteredCats)
            }
        }
    }


    private fun fetchAllCats() {
        viewModelScope.launch {
            println("CatListViewModel: Fetching all cats...")

            setState { copy(loading = true) }
            try {
                val cats = withContext(Dispatchers.IO) {
                    repository.fetchAllCats().map { it.asCatListUiModel() }
                }
                println("CatListViewModel: Successfully fetched cats: $cats")
                setState { copy(cats = cats) }
            } catch (error: Exception) {
                println("CatListViewModel: Error fetching cats: $error")
                // TODO Handle error
            } finally {
                setState { copy(loading = false) }
            }
        }
    }



    private fun CatApiModel.asCatListUiModel() = CatListUiModel(
        id = this.id,
        name = this.name,
        alt_names = this.alt_names,
        description = this.description,
        temperament =  this.temperament
    )
}