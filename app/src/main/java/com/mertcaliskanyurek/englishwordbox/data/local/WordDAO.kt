package com.mertcaliskanyurek.englishwordbox.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mertcaliskanyurek.englishwordbox.data.model.WordLevel
import com.mertcaliskanyurek.englishwordbox.data.model.WordModel
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDAO {

    @Query("SELECT * FROM words WHERE level=:wordLevel AND useless = 0 AND knowCount < 3 ORDER BY RANDOM() LIMIT 1")
    fun getWordByLevel(wordLevel: WordLevel): Flow<WordModel>

    @Insert
    suspend fun insertAll(wordList: List<WordModel>)

    @Update
    suspend fun updateWord(word:WordModel)

    @Query("SELECT count(_id) FROM words WHERE level=:wordLevel AND (useless = 1 OR knowCount > 2)")
    fun getLearnedWordCount(wordLevel: WordLevel): Int
}