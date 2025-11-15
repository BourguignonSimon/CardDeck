package com.example.carddeck.ui.group

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carddeck.core.emotion.EmotionProvider
import com.example.carddeck.domain.model.EmotionCard
import com.example.carddeck.domain.repository.ResponseRepository
import com.example.carddeck.ui.navigation.NavArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class GroupViewViewModel @Inject constructor(
    private val responseRepository: ResponseRepository,
    private val emotionProvider: EmotionProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val sessionCode: String = requireNotNull(savedStateHandle[NavArgs.SESSION_CODE])

    private val _uiState = MutableStateFlow(GroupViewUiState())
    val uiState: StateFlow<GroupViewUiState> = _uiState

    private var observationJob: Job? = null

    init {
        observeResponses()
    }

    private fun observeResponses() {
        observationJob?.cancel()
        observationJob = viewModelScope.launch {
            responseRepository.observeAggregatedResponses(sessionCode)
                .onStart { _uiState.update { it.copy(isLoading = true, errorMessage = null) } }
                .catch { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message ?: "Unable to load responses")
                    }
                }
                .collect { counts ->
                    val cards = emotionProvider.getEmotionCards()
                    val aggregates = counts.entries.mapNotNull { (id, count) ->
                        val card = cards.find { it.id == id } ?: return@mapNotNull null
                        EmotionAggregate(card, count)
                    }.sortedByDescending { it.count }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            aggregates = aggregates,
                            errorMessage = null
                        )
                    }
                }
        }
    }
}

data class GroupViewUiState(
    val isLoading: Boolean = false,
    val aggregates: List<EmotionAggregate> = emptyList(),
    val errorMessage: String? = null
)

data class EmotionAggregate(
    val emotionCard: EmotionCard,
    val count: Int
)
