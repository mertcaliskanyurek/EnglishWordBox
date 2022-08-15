package com.mertcaliskanyurek.englishwordbox.di.module

import android.content.Context
import androidx.room.Room
import com.mertcaliskanyurek.englishwordbox.data.local.AppDatabase
import com.mertcaliskanyurek.englishwordbox.data.local.WordDAO
import com.mertcaliskanyurek.englishwordbox.data.repository.WordRepository
import com.mertcaliskanyurek.englishwordbox.data.repository.WordRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * Created by Mert Çalışkanyürek on 06,December,2021
 */

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun appDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "englishwordbox.db").build()

    @Provides
    @Singleton
    fun wordDAO(db: AppDatabase): WordDAO = db.wordDAO()

    @Provides
    @Singleton
    fun wordRepository(db: AppDatabase): WordRepository = WordRepositoryImpl(db.wordDAO())
}