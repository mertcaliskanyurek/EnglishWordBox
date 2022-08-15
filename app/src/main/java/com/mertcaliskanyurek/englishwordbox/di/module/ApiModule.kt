package com.mertcaliskanyurek.englishwordbox.di.module

import com.mertcaliskanyurek.englishwordbox.data.network.WordBoxApi
import com.mertcaliskanyurek.englishwordbox.util.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun retrofitClient(): Retrofit = Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun createApi(retrofit: Retrofit): WordBoxApi = retrofit.create(WordBoxApi::class.java)
}