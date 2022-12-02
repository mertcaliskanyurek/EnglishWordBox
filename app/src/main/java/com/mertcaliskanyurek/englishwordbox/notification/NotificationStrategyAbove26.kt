package com.mertcaliskanyurek.englishwordbox.notification


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.mertcaliskanyurek.englishwordbox.R
import com.mertcaliskanyurek.englishwordbox.ui.view.MainActivity


@RequiresApi(Build.VERSION_CODES.O)
class NotificationStrategyAboveApi26(
    private val context: Context
) : NotificationStrategy {

    companion object {
        private const val CHANNEL_ID = "CoinPricesNotificationChannel"
    }

    private val notificationManager: NotificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    init {
        initChannel()
    }

    private fun initChannel() {
        val serviceChannel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(serviceChannel)
    }

    override fun buildNotification(title: String?, text: String?): Notification {
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.putExtra(MainActivity.EXTRA_WORD,text)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(
            context,
            0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(text)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .build()
    }

    override fun notify(id: Int, notification: Notification?) {
        notificationManager.notify(id, notification)
    }
}