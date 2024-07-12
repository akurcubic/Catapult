package com.example.catlistapp.profile.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catlistapp.profile.datastore.ProfileDataStore
import com.example.catlistapp.quiz.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileDataStore: ProfileDataStore,
    private val quizRepository: QuizRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileContract.ProfileState())
    val state = _state.asStateFlow()
    private fun setState(reducer: ProfileContract.ProfileState.() -> ProfileContract.ProfileState) =
        _state.update(reducer)

    private val events = MutableSharedFlow<ProfileContract.ProfileEvent>()
    fun setEvent(event: ProfileContract.ProfileEvent) = viewModelScope.launch { events.emit(event) }

    init {
        observeEvents()
        loadProfileData()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is ProfileContract.ProfileEvent.EditProfile -> {
                        if (event.name != state.value.name && state.value.isNameValid) {
                            profileDataStore.updateFullName(event.name)
                            setState { copy(name = event.name) }
                        }
                        if (event.email != state.value.email && state.value.isEmailValid) {
                            profileDataStore.updateEmail(event.email)
                            setState { copy(email = event.email) }
                        }
                        if (event.nickname != state.value.nickname && state.value.isNicknameValid) {
                            val oldNickname = state.value.nickname
                            val newNickname = event.nickname
                            profileDataStore.updateNickname(newNickname)
                            quizRepository.updateNickname(oldNickname, newNickname)
                            loadProfileData()
                            setState { copy(nickname = newNickname) }
                        }
                    }

                    is ProfileContract.ProfileEvent.OnNameChange -> {
                        setState { copy(isNameValid = event.name.isNotBlank()) }
                    }

                    is ProfileContract.ProfileEvent.OnNicknameChange -> {
                        setState { copy(isNicknameValid = event.nickname.matches(Regex("^[a-zA-Z0-9_]*$")) && event.nickname.isNotBlank())}
                    }

                    is ProfileContract.ProfileEvent.OnEmailChange -> {
                        setState {
                            copy(
                                isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(
                                    event.email
                                ).matches()
                            )
                        }
                    }
                }
            }
        }
    }


    private fun loadProfileData() {
        viewModelScope.launch {
            val profileData = profileDataStore.data.first()
            val quizResultsFlow = quizRepository.getAllQuizResults(profileData.nickname)
            val bestScore = quizRepository.getBestScore(profileData.nickname) ?: 0f
            val bestPosition =
                if (quizRepository.getBestPosition(profileData.nickname) == null) 0 else quizRepository.getBestPosition(
                    profileData.nickname
                )!!

            quizResultsFlow.collect { quizResults ->
                setState {
                    copy(
                        name = profileData.fullName,
                        nickname = profileData.nickname,
                        email = profileData.email,
                        quizResults = quizResults,
                        bestScore = bestScore,
                        bestPosition = bestPosition
                    )
                }
            }
        }
    }
}