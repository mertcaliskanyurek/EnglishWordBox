package com.mertcaliskanyurek.englishwordbox.helper

interface SoundPlayer {
    fun prepare(url: String)
    fun playSound()
    fun release()
}