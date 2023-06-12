package com.mertcaliskanyurek.englishwordbox.helper

import android.media.MediaPlayer
import android.util.Log
import java.io.IOException


class SoundPlayerImpl : SoundPlayer {
    private val TAG = SoundPlayerImpl::class.java.name

    private val mediaPlayer = MediaPlayer()
    private var prepared: Boolean = false

    override fun prepare(url: String) {
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(url)
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