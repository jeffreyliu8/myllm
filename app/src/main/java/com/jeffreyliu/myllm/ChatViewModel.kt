package com.jeffreyliu.myllm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeffreyliu.myllm.repository.InferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class ChatViewModel @Inject constructor(
    private var inferenceRepository: InferenceRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(inferenceRepository.uiState)
    val uiState: StateFlow<UiState> =_uiState.asStateFlow()

    private val _tokensRemaining = MutableStateFlow(-1)
    val tokensRemaining: StateFlow<Int> = _tokensRemaining.asStateFlow()

    private val _textInputEnabled: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isTextInputEnabled: StateFlow<Boolean> = _textInputEnabled.asStateFlow()

    private val _currentModel = MutableStateFlow(inferenceRepository.currentModel)
    val currentModel: StateFlow<Model> = _currentModel.asStateFlow()

    fun resetInferenceRepository() {
        inferenceRepository.resetModel()
        _uiState.value = inferenceRepository.uiState
        _currentModel.value = inferenceRepository.currentModel
    }

    fun sendMessage(userMessage: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value.addMessage(userMessage, USER_PREFIX)
            _uiState.value.createLoadingMessage()
            setInputEnabled(false)
            try {
                val asyncInference = inferenceRepository.generateResponseAsync(userMessage, { partialResult, done ->
                    _uiState.value.appendMessage(partialResult, done)
                    if (done) {
                        setInputEnabled(true)  // Re-enable text input
                    } else {
                        // Reduce current token count (estimate only). sizeInTokens() will be used
                        // when computation is done
                        _tokensRemaining.update { max(0, it - 1) }
                    }
                })
                // Once the inference is done, recompute the remaining size in tokens
                asyncInference.addListener({
                    viewModelScope.launch(Dispatchers.IO) {
                        recomputeSizeInTokens(userMessage)
                    }
                }, Dispatchers.Main.asExecutor())
            } catch (e: Exception) {
                _uiState.value.addMessage(e.localizedMessage ?: "Unknown Error", MODEL_PREFIX)
                setInputEnabled(true)
            }
        }
    }

    private fun setInputEnabled(isEnabled: Boolean) {
        _textInputEnabled.value = isEnabled
    }

    fun recomputeSizeInTokens(message: String) {
        val remainingTokens = inferenceRepository.estimateTokensRemaining(message)
        _tokensRemaining.value = remainingTokens
    }

    fun resetSession() {
        inferenceRepository.resetSession()
        _uiState.value.clearMessages()
        recomputeSizeInTokens("")
    }

    fun closeModel() {
        inferenceRepository.close()
        _uiState.value.clearMessages()
        recomputeSizeInTokens("")
    }
}
