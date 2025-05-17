package com.jeffreyliu.myllm.viewmodel

import androidx.lifecycle.ViewModel
import com.jeffreyliu.myllm.Model
import com.jeffreyliu.myllm.repository.InferenceRepository
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val inferenceRepository: Lazy<InferenceRepository>,
) : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState


    fun setModel(model: Model) {
        inferenceRepository.get().setModel(model)
        _uiState.update {
            it.copy(
                model = model,
            )
        }
    }

    fun onResetInstance() {
        inferenceRepository.get().resetModel()
    }

    fun isInferenceModelExist(): Boolean {
        return inferenceRepository.get().modelExists()
    }

    fun modelPathFromUrl(): String {
        return inferenceRepository.get().modelPathFromUrl()
    }
}

data class GameUiState(
    val model: Model = Model.PHI4_CPU,
)