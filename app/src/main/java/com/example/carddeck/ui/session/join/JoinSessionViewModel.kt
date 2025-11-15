package com.example.carddeck.ui.session.join

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
class JoinSessionViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(JoinSessionUiState())
    val uiState: StateFlow<JoinSessionUiState> = _uiState

    fun onCodeChanged(value: String) {
        val sanitized = value.filter { it.isLetterOrDigit() }.uppercase().take(6)
        _uiState.update { it.copy(codeInput = sanitized, errorMessage = null) }
    }

    fun joinSession() {
        val code = _uiState.value.codeInput.trim()
        if (code.length != 6) {
            _uiState.update { it.copy(errorMessage = "Enter a 6-character code") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val session = sessionRepository.findSessionByCode(code)
            if (session != null) {
                _uiState.update { it.copy(isLoading = false, joinedSessionCode = session.code) }
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Session not found") }
            }
        }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(joinedSessionCode = null) }
    }
}

data class JoinSessionUiState(
    val codeInput: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val joinedSessionCode: String? = null
)
