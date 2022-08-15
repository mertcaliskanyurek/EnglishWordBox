package com.mertcaliskanyurek.englishwordbox.di.module

import android.content.Context
import com.mertcaliskanyurek.englishwordbox.helper.SoundPlayer
import com.mertcaliskanyurek.englishwordbox.helper.SoundPlayerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HelperModule {

    @Provides
    @Singleton
    fun soundPlayer(@ApplicationContext context: Context): SoundPlayer = SoundPlayerImpl(context)
}