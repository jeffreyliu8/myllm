package com.jeffreyliu.myllm.viewmodel

import androidx.lifecycle.ViewModel
import com.jeffreyliu.myllm.Model
import com.jeffreyliu.myllm.repository.LLMModelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val llmModelRepository: LLMModelRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState


    fun setModel(model: Model) {
        _uiState.update {
            it.copy(
                model = model,
            )
        }
    }
}

data class GameUiState(
    val model: Model = Model.PHI4_CPU
)