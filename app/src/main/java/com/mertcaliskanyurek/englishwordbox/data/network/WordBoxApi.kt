package com.mertcaliskanyurek.englishwordbox.data.network

import com.mertcaliskanyurek.englishwordbox.data.model.WordModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WordBoxApi {

    @GET("picture.php")
    suspend fun picture(@Query("query") query: String,@Query("api_key") apiKey: String): Response<String>

    @GET("words.php")
    suspend fun words(
        @Query("lang") lang: String, @Query("letter") letter: String,
        @Query("api_key") apiKey: String
    ): Response<List<WordModel>>

    @GET("report.php")
    suspend fun report(
        @Query("word") word: String, @Query("reason") reason: String, @Query("api_key") apiKey: String
    ): Response<String>
}