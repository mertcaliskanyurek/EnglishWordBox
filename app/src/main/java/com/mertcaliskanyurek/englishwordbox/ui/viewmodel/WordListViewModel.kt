package com.mertcaliskanyurek.englishwordbox.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertcaliskanyurek.englishwordbox.data.model.WordModel
import com.mertcaliskanyurek.englishwordbox.data.model.WordState
import com.mertcaliskanyurek.englishwordbox.data.network.WordBoxApi
import com.mertcaliskanyurek.englishwordbox.data.repository.WordRepository
import com.mertcaliskanyurek.englishwordbox.util.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordListViewModel @Inject constructor(
    private val wordRepository: WordRepository,
    private val wordBoxApi: WordBoxApi,
) : ViewModel() {

    val words: MutableLiveData<List<WordModel>> by lazy {
        MutableLiveData()
    }

    val pictureUrl: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    fun prepareWords(wordState: WordState) = viewModelScope.launch(Dispatchers.IO) {
        wordRepository.getWords(wordState).collect() {
            words.postValue(it)
        }
    }

    fun prepareWordImage(word: String) = viewModelScope.launch(Dispatchers.IO) {
        val res = wordBoxApi.picture(word, AppConstants.API_KEY)
        if(res.isSuccessful) pictureUrl.postValue(res.body())
    }

    fun restore(id: Long, wordState: WordState) = viewModelScope.launch(Dispatchers.IO) {
        wordRepository.updateState(id,wordState)
    }

    fun putBack(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        wordRepository.updateState(id,WordState.NOTHING)
    }

}