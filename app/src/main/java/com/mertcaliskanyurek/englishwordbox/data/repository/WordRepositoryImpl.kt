package com.mertcaliskanyurek.englishwordbox.data.repository

import com.mertcaliskanyurek.englishwordbox.data.local.WordDAO
import com.mertcaliskanyurek.englishwordbox.data.model.WordModel
import com.mertcaliskanyurek.englishwordbox.data.model.WordState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


/**
 * Created by Mert Çalışkanyürek on 06,December,2021
 */

class WordRepositoryImpl @Inject constructor(
    private val db: WordDAO
):WordRepository {

    override suspend fun insert(word: WordModel) = db.insert(word)

    override suspend fun insertAll(wordList: List<WordModel>) = db.insertAll(wordList)

    override suspend fun updateWord(word: WordModel) = db.updateWord(word)

    override suspend fun deleteWord(word: WordModel) = db.deleteWord(word)

    override fun searchWord(text: String): Flow<List<WordModel>> = db.searchWord(text)

    override fun getWords(state: WordState): Flow<List<WordModel>> = db.getWords(state)

    override suspend fun updateState(id: Long, state: WordState) = db.setState(id,state)

    override fun deleteAll() = db.nukeTable()

    override fun getRandomFromBox(): Flow<WordModel?> = db.getRandom(WordState.IN_BOX)
}