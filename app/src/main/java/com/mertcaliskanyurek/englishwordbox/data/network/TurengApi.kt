package com.mertcaliskanyurek.englishwordbox.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TurengApi {

    @GET("tureng")
    suspend fun translate(
        @Query("lang") lang: String, @Query("input_lang") inputLang: String,
        @Query("word") word: String, @Query("api_key") apiKey: String
    ): Response<TranslationResponse>
}