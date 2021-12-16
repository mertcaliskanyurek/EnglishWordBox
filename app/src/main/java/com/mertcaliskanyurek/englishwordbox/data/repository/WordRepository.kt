package com.mertcaliskanyurek.englishwordbox.data.repository

import androidx.lifecycle.LiveData
import com.mertcaliskanyurek.englishwordbox.data.local.WordDAO
import com.mertcaliskanyurek.englishwordbox.data.model.WordLevel
import com.mertcaliskanyurek.englishwordbox.data.model.WordModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


/**
 * Created by Mert Çalışkanyürek on 06,December,2021
 */

class WordRepository @Inject constructor(
    private val db: WordDAO
):IWordRepository {

    override fun getWordsByLevel(level: WordLevel): LiveData<List<WordModel>> = db.getWordsByLevel(level)

    override suspend fun insertAll(wordList: List<WordModel>) = db.insertAll(wordList)

    override suspend fun updateWord(word: WordModel) = db.updateWord(word)
}