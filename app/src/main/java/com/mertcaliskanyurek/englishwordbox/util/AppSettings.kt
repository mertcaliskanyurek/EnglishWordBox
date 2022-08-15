package com.mertcaliskanyurek.englishwordbox.util

import android.content.Context
import android.content.SharedPreferences
import java.util.*

object AppSettings {

    fun getFirstTime(context: Context): Boolean {
        val pref = getPrefences(context)
        val key = "FIRST_TIME"
        return pref.getBoolean(key,true)
    }

    fun setFirstTime(context: Context, firstTime: Boolean) {
        val pref = getPrefences(context).edit()
        val key = "FIRST_TIME"
        pref.putBoolean(key,firstTime)
        pref.apply()
    }

    fun getMotherTongue(context: Context): String {
        val pref = getPrefences(context)
        val key = "MOTHER_TONGUE"
        val tongue = pref.getString(key,"tr")
        return tongue ?: "tr"
    }

    fun setMotherTongue(context: Context, tongue: String) {
        val pref = getPrefences(context).edit()
        val key = "MOTHER_TONGUE";
        pref.putString(key,tongue)
        pref.apply()
    }

    private fun getPrefences(context: Context): SharedPreferences {
        return context.getSharedPreferences("wordbox_settings", Context.MODE_PRIVATE);
    }

}