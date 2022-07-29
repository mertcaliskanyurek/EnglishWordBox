package com.mertcaliskanyurek.englishwordbox.ui.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertcaliskanyurek.englishwordbox.data.model.WordLevel
import com.mertcaliskanyurek.englishwordbox.data.model.WordModel
import com.mertcaliskanyurek.englishwordbox.data.repository.WordRepository
import com.mertcaliskanyurek.englishwordbox.util.AppSettings
import com.mertcaliskanyurek.englishwordbox.util.GsonUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class FirstStartViewModel @Inject constructor(
    private val application: Application,
    private val wordRepository: WordRepository
) : ViewModel(){

    val progress: MutableLiveData<Float> by lazy {
        MutableLiveData<Float>(0.0f)
    }

    val selected: MutableLiveData<Boolean> = MutableLiveData(false)

    private fun prepareWords(motherTongue : String) {
        viewModelScope.launch(Dispatchers.IO) {
            wordRepository.deleteAll()
            WordLevel.values().forEach { level ->
                progress.postValue(progress.value?.plus(0.25f) ?: 1.0f)
                val wordList = GsonUtil.getWords(application.applicationContext,motherTongue+"/"+level.name+".json")
                wordList.forEach(){
                    it.level = level.name
                }
                wordRepository.insertAll(wordList)
            }

            AppSettings.setFirstTime(application.applicationContext,false)
        }
    }

    fun onMotherTongueSelected(tongue: Int) {
        selected.postValue(true)
        when(tongue) {
            1 -> prepareWords("tr")
            2 -> prepareWords("de")
            3 -> prepareWords("es")
            4 -> prepareWords("fr")
        }
    }
}