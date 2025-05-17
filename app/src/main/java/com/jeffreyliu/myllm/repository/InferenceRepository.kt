package com.jeffreyliu.myllm.repository

import android.content.Context
import com.google.common.util.concurrent.ListenableFuture
import com.jeffreyliu.myllm.Model
import com.jeffreyliu.myllm.InferenceModel
import com.jeffreyliu.myllm.UiState
import com.google.mediapipe.tasks.genai.llminference.ProgressListener
import javax.inject.Inject

/**
 * Repository for handling inference operations.
 * This class encapsulates the InferenceModel and provides a clean interface for the ViewModel.
 */
class InferenceRepository @Inject constructor(private val context: Context) {

    private lateinit var inferenceModel: InferenceModel

    val uiState: UiState
        get() = inferenceModel.uiState

    val currentModel: Model
        get() = InferenceModel.model

    fun setModel(model: Model) {
        InferenceModel.model = model
    }

    fun modelExists(): Boolean {
        return InferenceModel.modelExists(context)
    }

    fun modelPathFromUrl(): String {
        return InferenceModel.modelPathFromUrl(context)
    }

    fun resetSession() {
        inferenceModel.resetSession()
    }

    fun close() {
        inferenceModel.close()
    }

    fun resetModel() {
        inferenceModel = InferenceModel.resetInstance(context)
    }

    fun generateResponseAsync(prompt: String, progressListener: ProgressListener<String>): ListenableFuture<String> {
        return inferenceModel.generateResponseAsync(prompt, progressListener)
    }

    fun estimateTokensRemaining(prompt: String): Int {
        return inferenceModel.estimateTokensRemaining(prompt)
    }
}