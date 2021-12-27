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

    companion object {
        val WORD_COUNT = 248
    }

    override fun getWord(level: WordLevel): Flow<WordModel> = db.getWordByLevel(level)

    override suspend fun insertAll(wordList: List<WordModel>) = db.insertAll(wordList)

    override suspend fun updateWord(word: WordModel) = db.updateWord(word)

    override fun getProgress(level: WordLevel): Int {
        val learned: Int = db.getLearnedWordCount(level)
        return (learned * 100) / WORD_COUNT
    }
}