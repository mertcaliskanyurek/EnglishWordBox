package com.mertcaliskanyurek.englishwordbox.notification

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.mertcaliskanyurek.englishwordbox.R


class NotificationStrategyBelowApi26(
    private val context: Context
) : NotificationStrategy{

    private val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    override fun buildNotification(title: String?, text: String?): Notification {
        return NotificationCompat.Builder(context)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(text)
            .build()
    }

    override fun notify(id: Int, notification: Notification?) {
        notificationManager.notify(
            id,
            notification
        )
    }
}