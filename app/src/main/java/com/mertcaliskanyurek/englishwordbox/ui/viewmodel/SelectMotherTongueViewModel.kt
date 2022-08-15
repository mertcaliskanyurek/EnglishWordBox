package com.mertcaliskanyurek.englishwordbox.ui.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertcaliskanyurek.englishwordbox.App
import com.mertcaliskanyurek.englishwordbox.data.network.WordBoxApi
import com.mertcaliskanyurek.englishwordbox.data.repository.WordRepository
import com.mertcaliskanyurek.englishwordbox.data.repository.WordRepositoryImpl
import com.mertcaliskanyurek.englishwordbox.util.AppConstants
import com.mertcaliskanyurek.englishwordbox.util.AppSettings
import com.mertcaliskanyurek.englishwordbox.util.GsonUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class SelectMotherTongueViewModel @Inject constructor(
    private val application: Application,
    private val wordRepository: WordRepository,
    private val wordBoxApi: WordBoxApi
) : ViewModel(){

    val progress: MutableLiveData<Float> by lazy {
        MutableLiveData<Float>(0.0f)
    }

    val error: MutableLiveData<String> by lazy {
        MutableLiveData(null)
    }

    val selected: MutableLiveData<Boolean> = MutableLiveData(false)

    private fun prepareWords(translate : String) {
        viewModelScope.launch(Dispatchers.IO) {
            wordRepository.deleteAll()
            AppConstants.LETTERS.forEach { letter ->
                val wordsResp = wordBoxApi.words(translate,letter.toString(),AppConstants.API_KEY)
                if (wordsResp.isSuccessful) {
                    wordRepository.insertAll(wordsResp.body()!!)
                    progress.postValue(progress.value?.plus(0.25f) ?: 1.0f)
                }
                else {
                    error.postValue(wordsResp.message())
                    return@forEach
                }
            }
            //wordRepository.insertAll(wordList)
            AppSettings.setFirstTime(application.applicationContext,false)
        }
    }

    fun onMotherTongueSelected(translation: String) {
        selected.postValue(true)
        prepareWords(translation)
        /*when(tongue) {
            1 -> prepareWords(AppConstants.TRANSLATION_EN_TR)
            2 -> prepareWords(AppConstants.TRANSLATION_EN_DE)
            3 -> prepareWords(AppConstants.TRANSLATION_EN_ES)
            4 -> prepareWords(AppConstants.TRANSLATION_EN_FR)
        }*/
    }
}