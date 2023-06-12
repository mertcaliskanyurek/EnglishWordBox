package com.mertcaliskanyurek.englishwordbox.di.module

import android.content.Context
import android.os.Build
import com.mertcaliskanyurek.englishwordbox.helper.SoundPlayer
import com.mertcaliskanyurek.englishwordbox.helper.SoundPlayerImpl
import com.mertcaliskanyurek.englishwordbox.notification.NotificationStrategy
import com.mertcaliskanyurek.englishwordbox.notification.NotificationStrategyAboveApi26
import com.mertcaliskanyurek.englishwordbox.notification.NotificationStrategyBelowApi26
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
    fun soundPlayer(): SoundPlayer = SoundPlayerImpl()

    @Provides
    fun notificationStrategy(@ApplicationContext context: Context): NotificationStrategy =
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) NotificationStrategyAboveApi26(context)
        else NotificationStrategyBelowApi26(context)
}