package com.mertcaliskanyurek.englishwordbox.data.repository

import androidx.lifecycle.LiveData
import com.mertcaliskanyurek.englishwordbox.data.model.WordLevel
import com.mertcaliskanyurek.englishwordbox.data.model.WordModel

interface IWordRepository {

    fun getWordsByLevel(level: WordLevel) : LiveData<List<WordModel>>
    suspend fun insertAll(wordList: List<WordModel>)
    suspend fun updateWord(word: WordModel)
}