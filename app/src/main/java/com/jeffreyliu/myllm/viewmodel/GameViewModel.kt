package com.jeffreyliu.myllm.viewmodel

import androidx.lifecycle.ViewModel
import com.jeffreyliu.myllm.InferenceModel
import com.jeffreyliu.myllm.Model
import com.jeffreyliu.myllm.repository.InferenceModelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val inferenceModelRepository: InferenceModelRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState


    fun setModel(model: Model) {
        inferenceModelRepository.setModel(model)

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