package com.mertcaliskanyurek.englishwordbox.notification

import android.app.Notification

interface NotificationStrategy {
    fun buildNotification(title: String?, text: String?): Notification?
    fun notify(id: Int, notification: Notification?)
}