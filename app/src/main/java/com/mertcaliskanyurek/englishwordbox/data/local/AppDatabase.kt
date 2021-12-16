package com.mertcaliskanyurek.englishwordbox.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mertcaliskanyurek.englishwordbox.data.model.WordModel


/**
 * Created by Mert Çalışkanyürek on 06,December,2021
 */

@Database(entities = [
                WordModel::class
                     ], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDAO(): WordDAO
}