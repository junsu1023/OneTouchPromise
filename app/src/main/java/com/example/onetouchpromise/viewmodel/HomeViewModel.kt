package com.example.onetouchpromise.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetMeetingsUseCase
import com.example.onetouchpromise.Contract.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMeetingsUseCase: GetMeetingsUseCase
): ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    init {
        observeMeetings()
    }

    private fun observeMeetings() {
        viewModelScope.launch {
            getMeetingsUseCase()
                .onEach { meetings ->
                    uiState = uiState.copy(
                        meetings = meetings,
                        isLoading = false
                    )
                }
                .catch { e ->
                    uiState = uiState.copy(
                        error = e.message,
                        isLoading = false

                    )
                }
                .collect()
        }
    }
}