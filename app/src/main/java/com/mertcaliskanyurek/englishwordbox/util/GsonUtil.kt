package com.mertcaliskanyurek.englishwordbox.util

import android.content.Context
import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mertcaliskanyurek.englishwordbox.data.model.WordModel

object GsonUtil {

    private fun AssetManager.readAssetsFile(fileName : String): String = open(fileName).bufferedReader().use{it.readText()}

    fun getWords(context: Context,fileName: String): List<WordModel> = Gson().fromJson(context.assets.readAssetsFile(fileName), object :
        TypeToken<List<WordModel>>(){}.type)
}