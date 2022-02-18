package com.mertcaliskanyurek.englishwordbox.data.network

data class TranslationResponse(
    val word: String,
    val means: List<String>,
    val sound: String,
)
