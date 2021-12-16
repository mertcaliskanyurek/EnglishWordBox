package com.mertcaliskanyurek.englishwordbox.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class WordModel(
    @PrimaryKey(autoGenerate = true) val _id: Long = 0,
    val word: String,
    val translateTR: String,
    val level: WordLevel,
    val isInBox: Boolean = true,
)