package com.mertcaliskanyurek.englishwordbox.helper

import android.R
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.core.content.PackageManagerCompat.LOG_TAG
import java.io.IOException


class SoundPlayerImpl(private val context: Context) : SoundPlayer {
    private val TAG = SoundPlayerImpl::class.java.name

    private val mediaPlayer = MediaPlayer()
    private var prepared: Boolean = false

    override fun prepare(url: String) {
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(context,Uri.parse(url))
            mediaPlayer.setOnPreparedListener {
                prepared = true
            }
            mediaPlayer.prepareAsync()
        } catch (e: IOException) {
            Log.w(TAG, "MP Error $e")
        }
    }

    override fun playSound() {
        if (mediaPlayer.isPlaying || !prepared) return
        mediaPlayer.start()
    }

    override fun release() {
        mediaPlayer.release()
    }
}