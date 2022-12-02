package com.mertcaliskanyurek.englishwordbox.data.repository

import com.mertcaliskanyurek.englishwordbox.data.model.WordModel
import com.mertcaliskanyurek.englishwordbox.data.model.WordState
import kotlinx.coroutines.flow.Flow

interface WordRepository {

    suspend fun insert(word: WordModel)
    suspend fun insertAll(wordList: List<WordModel>)
    suspend fun updateWord(word: WordModel)
    suspend fun deleteWord(word: WordModel)

    /**
     * Searchs word beginning from first latter and related letters
     *
     * @param text search text supposed to be a single word
     * @return Flow of word list related search text
     */
    fun searchWord(text: String) : Flow<List<WordModel>>

    /**
     * Returns words that are in box, trash or neither of them
     *
     * @param state must be one of [com.mertcaliskanyurek.englishwordbox.data.model.WordState]
     * @return words in box or in trash
     */
    fun getWords(state: WordState): Flow<List<WordModel>>

    /**
     * Updates word state
     *
     * @param id word id
     * @param state must be one of [com.mertcaliskanyurek.englishwordbox.data.model.WordState]
     */
    suspend fun updateState(id: Long, state: WordState)

    /**
     * Deletes all words from table
     */
    fun deleteAll()


    /**
     * @return random word from saved in box
     */
    fun getRandomFromBox(): Flow<WordModel?>
}