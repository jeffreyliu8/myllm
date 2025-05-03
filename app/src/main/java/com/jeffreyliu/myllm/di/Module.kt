package com.jeffreyliu.myllm.di

import com.jeffreyliu.myllm.repository.LLMModelRepository
import com.jeffreyliu.myllm.repository.LLMModelRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindGameRepository(llmMModelRepositoryImpl: LLMModelRepositoryImpl): LLMModelRepository
}