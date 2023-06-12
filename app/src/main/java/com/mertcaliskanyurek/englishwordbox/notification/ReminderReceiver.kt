package com.mertcaliskanyurek.englishwordbox.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.mertcaliskanyurek.englishwordbox.data.repository.WordRepository
import com.mertcaliskanyurek.englishwordbox.util.ReminderUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {

    @Inject lateinit var wordRepository: WordRepository
    @Inject lateinit var notificationStrategy: NotificationStrategy

    companion object {
        private const val NOTIFICATION_ID = 1
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            ReminderUtil.initNextReminder(it)
        }
        CoroutineScope(Dispatchers.Default).launch {
            wordRepository.getRandomFromBox().collect { word->
                word?.let {
                    Log.d("NOTI","${it.word} is being notified")
                    val noti = notificationStrategy.buildNotification("",it.word)
                    notificationStrategy.notify(NOTIFICATION_ID, noti)
                }
            }
        }
    }
}