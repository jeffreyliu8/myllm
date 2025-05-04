package com.jeffreyliu.myllm.di

import android.content.Context
import com.jeffreyliu.myllm.InferenceModel
import com.jeffreyliu.myllm.repository.InferenceModelRepository
import com.jeffreyliu.myllm.repository.InferenceModelRepositoryImpl
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
//    @Singleton
    abstract fun bindInferenceModelRepositoryImpl(llmMModelRepositoryImpl: InferenceModelRepositoryImpl): InferenceModelRepository
}

//@Module
//@InstallIn(SingletonComponent::class)
//object AppModule {
//    @Provides
//    @Singleton
//    fun provideInferenceModel(@ApplicationContext context: Context): InferenceModel {
//        return InferenceModel.getInstance(
//            context = context,
//        )
//    }
//}