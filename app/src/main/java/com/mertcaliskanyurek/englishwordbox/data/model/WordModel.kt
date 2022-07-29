package com.mertcaliskanyurek.englishwordbox.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class WordModel(
    @PrimaryKey(autoGenerate = true) val _id: Long = 0,
    val word: String,
    val means: String,
    val sound: String,
    var level: String = "A1",
    var box: Boolean = false,
    var trash: Boolean = false
)