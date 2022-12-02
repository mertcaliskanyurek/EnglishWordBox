package com.mertcaliskanyurek.englishwordbox.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.preference.PreferenceManager
import com.mertcaliskanyurek.englishwordbox.notification.ReminderReceiver
import java.util.*

object ReminderUtil {

    //const val ONE_HOUR_IN_MILLS = 3600000

    fun initNextReminder(context: Context) {
        val calendar: Calendar = Calendar.getInstance()
        val period = PreferenceManager.getDefaultSharedPreferences(context).getInt("reminder",3)
        val alarmMgr: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(context,
            200,
            intent,
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_MUTABLE else 0)
        calendar.timeInMillis = System.currentTimeMillis()
        var nextHour = calendar.get(Calendar.HOUR_OF_DAY) + (12/period).toInt()
        if(nextHour > 22) nextHour = 9
        calendar.set(Calendar.HOUR_OF_DAY,nextHour)
        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmIntent)
    }
}