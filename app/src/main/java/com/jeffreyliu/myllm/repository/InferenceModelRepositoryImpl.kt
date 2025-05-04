package com.jeffreyliu.myllm.repository

import android.content.Context
import com.google.common.util.concurrent.ListenableFuture
import com.google.mediapipe.tasks.genai.llminference.ProgressListener
import com.jeffreyliu.myllm.InferenceModel
import com.jeffreyliu.myllm.Model
import com.jeffreyliu.myllm.UiState
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class InferenceModelRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : InferenceModelRepository {

    private lateinit var inferenceModel: InferenceModel

    init {
        resetInferenceModel()
    }

    private fun resetInferenceModel() {
        inferenceModel = InferenceModel.getInstance(context)
    }

    override fun setModel(model: Model) {
        InferenceModel.model = model
    }

    override fun getModel(): Model {
        return InferenceModel.model
    }

    override fun getInstance() {
        resetInferenceModel()
    }

    override fun getUiState(): UiState {
        return inferenceModel.uiState
    }

    override fun generateResponseAsync(
        prompt: String,
        progressListener: ProgressListener<String>
    ): ListenableFuture<String> {
        return inferenceModel.generateResponseAsync(prompt, progressListener)
    }

    override fun estimateTokensRemaining(prompt: String): Int {
        return inferenceModel.estimateTokensRemaining(prompt)
    }

    override fun resetSession() {
        return inferenceModel.resetSession()
    }

    override fun close() {
        return inferenceModel.close()
    }
}