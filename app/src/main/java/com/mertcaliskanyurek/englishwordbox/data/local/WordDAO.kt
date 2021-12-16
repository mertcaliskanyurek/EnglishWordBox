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

    @Query("SELECT * FROM words WHERE level=:wordLevel")
    fun getWordsByLevel(wordLevel: WordLevel): LiveData<List<WordModel>>

    @Insert
    suspend fun insertAll(wordList: List<WordModel>)

    @Update
    suspend fun updateWord(word:WordModel)
}