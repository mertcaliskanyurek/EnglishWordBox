package com.mertcaliskanyurek.englishwordbox.di.module

import com.mertcaliskanyurek.englishwordbox.data.network.TurengApi
import com.mertcaliskanyurek.englishwordbox.util.AppInfo
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
            .baseUrl(AppInfo.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun createApi(retrofit: Retrofit): TurengApi = retrofit.create(TurengApi::class.java)
}