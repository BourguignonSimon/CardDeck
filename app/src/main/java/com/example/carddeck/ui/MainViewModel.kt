package com.example.carddeck.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carddeck.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        viewModelScope.launch {
            runCatching { authRepository.ensureAnonymousUser() }
                .onSuccess { _uiState.update { it.copy(isReady = true, errorMessage = null) } }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isReady = false,
                            errorMessage = error.message ?: "Authentication failed"
                        )
                    }
                }
        }
    }
}

data class MainUiState(
    val isReady: Boolean = false,
    val errorMessage: String? = null
)
