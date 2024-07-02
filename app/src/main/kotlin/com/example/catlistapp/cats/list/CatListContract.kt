package com.example.catlistapp.cats.list

import com.example.catlistapp.cats.list.model.CatListUiModel

interface CatListContract {

    data class CatListState(
        val loading: Boolean = false,
        val query: String = "",
        val isSearchMode: Boolean = false,
        val cats: List<CatListUiModel> = emptyList(),
        val filteredCats: List<CatListUiModel> = emptyList(),
    )

    sealed class CatListUiEvent {
        data class SearchQueryChanged(val query: String) : CatListUiEvent()
        data object CloseSearchMode : CatListUiEvent()

    }
}