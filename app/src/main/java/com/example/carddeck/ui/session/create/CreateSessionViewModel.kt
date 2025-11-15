package com.example.carddeck.ui.session.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carddeck.domain.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CreateSessionViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateSessionUiState())
    val uiState: StateFlow<CreateSessionUiState> = _uiState

    init {
        createSession()
    }

    fun createSession() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching { sessionRepository.createSession() }
                .onSuccess { session ->
                    _uiState.update {
                        it.copy(
                            sessionCode = session.code,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message ?: "Unable to create session")
                    }
                }
        }
    }
}

data class CreateSessionUiState(
    val sessionCode: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
