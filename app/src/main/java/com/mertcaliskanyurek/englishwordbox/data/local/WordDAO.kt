package com.mertcaliskanyurek.englishwordbox.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mertcaliskanyurek.englishwordbox.data.model.WordLevel
import com.mertcaliskanyurek.englishwordbox.data.model.WordModel
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDAO {

    @Query("SELECT * FROM words WHERE level=:wordLevel AND trash = 0 ORDER BY RANDOM() LIMIT 1")
    fun getWordByLevel(wordLevel: WordLevel): Flow<WordModel>

    @Insert
    suspend fun insertAll(wordList: List<WordModel>)

    @Update
    suspend fun updateWord(word:WordModel)

    @Query("DELETE FROM words")
    fun nukeTable()

    @Query("SELECT * FROM words WHERE word LIKE '%' || :text || '%'")
    fun searchWord(text: String) : Flow<List<WordModel>>
}