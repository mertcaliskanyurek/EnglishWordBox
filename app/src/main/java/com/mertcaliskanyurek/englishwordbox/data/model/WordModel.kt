package com.mertcaliskanyurek.englishwordbox.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class WordModel(
    @PrimaryKey(autoGenerate = true) val _id: Long = 0,
    val word: String,
    val level: String,
    var translation: String = "",
    var sound: String = "",
    var knowCount: Int = 0,
    var useless: Boolean = false
)