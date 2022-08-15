package com.mertcaliskanyurek.englishwordbox.data.local

import androidx.room.*
import com.mertcaliskanyurek.englishwordbox.data.model.WordModel
import com.mertcaliskanyurek.englishwordbox.data.model.WordState
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDAO {

    @Insert
    suspend fun insert(word: WordModel)

    @Insert
    suspend fun insertAll(wordList: List<WordModel>)

    @Update
    suspend fun updateWord(word:WordModel)

    @Delete
    suspend fun deleteWord(word: WordModel)

    @Query("SELECT * FROM words WHERE word LIKE :text || '%' AND state != 'IN_TRASH' ")
    fun searchWord(text: String) : Flow<List<WordModel>>

    @Query("SELECT * FROM words WHERE state == :state")
    fun getWords(state: WordState): Flow<List<WordModel>>

    @Query("UPDATE words SET state = :state WHERE _id = :id")
    suspend fun setState(id: Long, state: WordState)

    @Query("SELECT state FROM words WHERE _id = :id")
    fun getState(id: Long): WordState

    @Query("DELETE FROM words")
    fun nukeTable()


}