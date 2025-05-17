package com.jeffreyliu.myllm.di

import android.content.Context
import com.jeffreyliu.myllm.repository.InferenceRepository
import com.jeffreyliu.myllm.repository.LLMModelRepository
import com.jeffreyliu.myllm.repository.LLMModelRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindGameRepository(llmMModelRepositoryImpl: LLMModelRepositoryImpl): LLMModelRepository
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideInferenceRepository(@ApplicationContext context: Context): InferenceRepository {
        return InferenceRepository(context)
    }
}