package com.mertcaliskanyurek.englishwordbox.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertcaliskanyurek.englishwordbox.data.network.WordBoxApi
import com.mertcaliskanyurek.englishwordbox.data.repository.WordRepository
import com.mertcaliskanyurek.englishwordbox.util.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class SelectMotherTongueViewModel @Inject constructor(
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
    var selectedTounge: String = ""

    private fun prepareWords(translate : String) {
        viewModelScope.launch(Dispatchers.IO) {
            wordRepository.deleteAll()
            AppConstants.LETTERS.forEach { letter ->
                val wordsResp = wordBoxApi.words(translate,letter.toString(),AppConstants.API_KEY)
                if (wordsResp.isSuccessful) {
                    wordRepository.insertAll(wordsResp.body()!!)
                    progress.postValue(progress.value?.plus(0.038f))
                }
                else {
                    error.postValue(wordsResp.message())
                    return@forEach
                }
            }

            progress.postValue(1.0f)
        }
    }

    fun onMotherTongueSelected(translation: String) {
        selectedTounge = translation
        selected.postValue(true)
        prepareWords(translation)
    }
}