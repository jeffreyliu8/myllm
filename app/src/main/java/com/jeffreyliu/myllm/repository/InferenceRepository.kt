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
    
    private var inferenceModel: InferenceModel = InferenceModel.getInstance(context)
    
    val uiState: UiState
        get() = inferenceModel.uiState
    
    val currentModel: Model
        get() = InferenceModel.model
    
    fun resetSession() {
        inferenceModel.resetSession()
    }
    
    fun close() {
        inferenceModel.close()
    }
    
    fun resetModel(): InferenceRepository {
        inferenceModel = InferenceModel.resetInstance(context)
        return this
    }
    
    fun generateResponseAsync(prompt: String, progressListener: ProgressListener<String>): ListenableFuture<String> {
        return inferenceModel.generateResponseAsync(prompt, progressListener)
    }
    
    fun estimateTokensRemaining(prompt: String): Int {
        return inferenceModel.estimateTokensRemaining(prompt)
    }
    
//    companion object {
//        private var instance: InferenceRepository? = null
//
//        fun getInstance(context: Context): InferenceRepository {
//            return instance ?: InferenceRepository(context).also { instance = it }
//        }
//
//        fun resetInstance(context: Context): InferenceRepository {
//            return InferenceRepository(context).also { instance = it }
//        }
//
//        fun modelExists(context: Context): Boolean {
//            return InferenceModel.modelExists(context)
//        }
//    }
}