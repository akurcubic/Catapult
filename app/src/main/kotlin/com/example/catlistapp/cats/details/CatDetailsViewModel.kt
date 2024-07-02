package com.example.catlistapp.cats.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catlistapp.cats.api.model.CatApiModel
import com.example.catlistapp.cats.details.model.CatDetailsUiModel
import com.example.catlistapp.cats.repository.CatsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class CatDetailsViewModel(
    private val catId: String,
    private val catsRepository: CatsRepository = CatsRepository
): ViewModel(){

    private val _state = MutableStateFlow(CatDetailsContract.CatDetailsState())
    val state = _state.asStateFlow()
    private fun setState(reducer: CatDetailsContract.CatDetailsState.() -> CatDetailsContract.CatDetailsState) = _state.update(reducer)

    init{

        fetchCatDetails()
    }


    private fun fetchCatDetails() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                val cat = withContext(Dispatchers.IO) {
                    catsRepository.fetchCatDetails(id = catId)
                }
                val catDetailsUiModel = cat.asCatDetailsUiModel()

                // Log the cat details
                Log.d("CatDetailsViewModel", "Cat details fetched: $catDetailsUiModel")

                setState { copy(cat = catDetailsUiModel) }
            } catch (error: IOException) {
                Log.e("CatDetailsViewModel", "Error fetching cat: $error")
            } finally {
                setState { copy(loading = false) }
            }
        }
    }


    private fun CatApiModel.asCatDetailsUiModel() = CatDetailsUiModel(
        id = this.id,
        name=this.name,
        temperament=this.temperament,
        origin=this.origin,
        description=this.description,
        life_span=this.life_span,
        alt_names=this.alt_names,
        adaptability=this.adaptability,
        affection_level=this.affection_level,
        stranger_friendly=this.stranger_friendly,
        dog_friendly=this.dog_friendly,
        energy_level=this.energy_level,
        social_needs=this.social_needs,
        health_issues=this.health_issues,
        intelligence=this.intelligence,
        rare = this.rare,
        wikipedia_url= this.wikipedia_url,
        reference_image_id = this.reference_image_id,
        weight=this.weight.metric
    )
}