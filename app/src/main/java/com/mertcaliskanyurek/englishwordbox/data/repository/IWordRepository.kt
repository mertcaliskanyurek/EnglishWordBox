package com.mertcaliskanyurek.englishwordbox.data.repository

import androidx.lifecycle.LiveData
import com.mertcaliskanyurek.englishwordbox.data.model.WordLevel
import com.mertcaliskanyurek.englishwordbox.data.model.WordModel
import kotlinx.coroutines.flow.Flow

interface IWordRepository {

    fun getWord(level: WordLevel) : Flow<WordModel>
    suspend fun insertAll(wordList: List<WordModel>)
    suspend fun updateWord(word: WordModel)
    fun getProgress(level: WordLevel): Int
}