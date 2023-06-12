package com.mertcaliskanyurek.englishwordbox.util

object AppConstants {
    val LETTERS = charArrayOf('a','b','c','d','e','f','g','h','i','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z')
    const val BASE_URL = "http://ormerlabs.com/api/wordbox/"
    const val API_KEY = "TO REQUEST YOUR API KEY E-MAIL info@ormerlabs.com"

    const val TRANSLATION_EN_TR = "en-tr"
    const val TRANSLATION_EN_DE = "en-de"
    const val TRANSLATION_EN_ES = "en-es"
    const val TRANSLATION_EN_FR = "en-fr"

    const val REASON_UNKNOWN_WORD: String = "unknown_word"

    enum class ErrorType{
        ERROR_WORD_NOT_FOUND, ERROR_FEEDBACK_NOT_SENT
    }
}