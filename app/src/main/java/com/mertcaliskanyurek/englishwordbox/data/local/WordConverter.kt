package com.mertcaliskanyurek.englishwordbox.data.local

import androidx.room.TypeConverter
import com.mertcaliskanyurek.englishwordbox.data.model.WordState

class WordConverter {
    @TypeConverter
    fun fromState(state: WordState?) : String = state?.name ?: WordState.NOTHING.name

    @TypeConverter
    fun toState(state: String) : WordState = enumValueOf<WordState>(state)
}