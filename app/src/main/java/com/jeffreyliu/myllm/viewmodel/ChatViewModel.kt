package com.jeffreyliu.myllm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeffreyliu.myllm.MODEL_PREFIX
import com.jeffreyliu.myllm.Model
import com.jeffreyliu.myllm.USER_PREFIX
import com.jeffreyliu.myllm.UiState
import com.jeffreyliu.myllm.repository.InferenceModelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val inferenceModelRepository: InferenceModelRepository,
) : ViewModel() {

    init {
        inferenceModelRepository.getInstance()
    }

    private val _chatScreenUiState =
        MutableStateFlow(
            ChatScreenUiState(
                model = inferenceModelRepository.getModel(),
                uiState = inferenceModelRepository.getUiState()
            )
        )
    val chatScreenUiState = _chatScreenUiState.asStateFlow()



    fun sendMessage(userMessage: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _chatScreenUiState.value.uiState.addMessage(userMessage, USER_PREFIX)
            _chatScreenUiState.value.uiState.createLoadingMessage()
            setInputEnabled(false)
            try {
                val asyncInference =
                    inferenceModelRepository.generateResponseAsync(
                        userMessage,
                        { partialResult, done ->
                            _chatScreenUiState.value.uiState.appendMessage(partialResult, done)
                            if (done) {
                                setInputEnabled(true)  // Re-enable text input
                            } else {
                                // Reduce current token count (estimate only). sizeInTokens() will be used
                                // when computation is done
//                            _tokensRemaining.update { max(0, it - 1) }

                                _chatScreenUiState.update { us ->
                                    us.copy(
                                        tokensRemaining = max(0, us.tokensRemaining - 1),
                                    )
                                }
                            }
                        })
                // Once the inference is done, recompute the remaining size in tokens
                asyncInference.addListener({
                    viewModelScope.launch(Dispatchers.IO) {
                        recomputeSizeInTokens(userMessage)
                    }
                }, Dispatchers.Main.asExecutor())
            } catch (e: Exception) {
                _chatScreenUiState.value.uiState.addMessage(
                    e.localizedMessage ?: "Unknown Error",
                    MODEL_PREFIX
                )
                setInputEnabled(true)
            }
        }
    }

    private fun setInputEnabled(isEnabled: Boolean) {
        _chatScreenUiState.update {
            it.copy(
                textInputEnabled = isEnabled,
            )
        }
    }

    fun recomputeSizeInTokens(message: String) {
        _chatScreenUiState.update {
            it.copy(
                tokensRemaining = inferenceModelRepository.estimateTokensRemaining(message),
            )
        }
    }

    fun onInferenceModelInstanceResetSession() {
        inferenceModelRepository.resetSession()
    }

    fun onInferenceModelInstanceCloseSession() {
        inferenceModelRepository.close()
    }
}

data class ChatScreenUiState(
    val model: Model,
    val tokensRemaining: Int = -1,
    val textInputEnabled: Boolean = true,
    val uiState: UiState,
)