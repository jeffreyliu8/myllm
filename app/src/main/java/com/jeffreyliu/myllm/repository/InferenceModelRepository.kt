package com.jeffreyliu.myllm.repository

import com.google.common.util.concurrent.ListenableFuture
import com.google.mediapipe.tasks.genai.llminference.ProgressListener
import com.jeffreyliu.myllm.Model
import com.jeffreyliu.myllm.UiState

interface InferenceModelRepository {
    fun setModel(model: Model)
    fun getModel(): Model
    fun getInstance()
    fun getUiState(): UiState

    fun generateResponseAsync(
        prompt: String,
        progressListener: ProgressListener<String>
    ): ListenableFuture<String>

    fun estimateTokensRemaining(prompt: String): Int
    fun resetSession()
    fun close()
}