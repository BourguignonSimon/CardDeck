package com.example.carddeck.ui.emotion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carddeck.core.emotion.EmotionProvider
import com.example.carddeck.domain.model.EmotionCard
import com.example.carddeck.domain.repository.ResponseRepository
import com.example.carddeck.ui.navigation.NavArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EmotionSelectionViewModel @Inject constructor(
    private val responseRepository: ResponseRepository,
    private val emotionProvider: EmotionProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val sessionCode: String = requireNotNull(savedStateHandle[NavArgs.SESSION_CODE])

    private val _uiState = MutableStateFlow(
        EmotionSelectionUiState(emotions = emotionProvider.getEmotionCards())
    )
    val uiState: StateFlow<EmotionSelectionUiState> = _uiState

    fun toggleEmotion(emotionId: String) {
        _uiState.update { state ->
            val current = state.selectedEmotionIds
            val newSelection = if (current.contains(emotionId)) {
                current - emotionId
            } else {
                if (current.size >= MAX_SELECTION) return
                current + emotionId
            }
            state.copy(selectedEmotionIds = newSelection, errorMessage = null)
        }
    }

    fun submitSelection() {
        val selected = _uiState.value.selectedEmotionIds
        if (selected.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Select at least one emotion") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
            runCatching { responseRepository.submitResponse(sessionCode, selected.toList()) }
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            selectedEmotionIds = emptySet(),
                            submissionCompleted = true
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            errorMessage = error.message ?: "Unable to submit"
                        )
                    }
                }
        }
    }

    fun onSubmissionHandled() {
        _uiState.update { it.copy(submissionCompleted = false) }
    }

    companion object {
        private const val MAX_SELECTION = 3
    }
}

data class EmotionSelectionUiState(
    val emotions: List<EmotionCard> = emptyList(),
    val selectedEmotionIds: Set<String> = emptySet(),
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val submissionCompleted: Boolean = false
)
