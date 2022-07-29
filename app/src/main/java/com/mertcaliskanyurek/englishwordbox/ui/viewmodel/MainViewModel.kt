package com.mertcaliskanyurek.englishwordbox.ui.viewmodel

import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertcaliskanyurek.englishwordbox.data.model.WordModel
import com.mertcaliskanyurek.englishwordbox.data.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Mert Çalışkanyürek on 08,December,2021
 */

@HiltViewModel
class MainViewModel @Inject constructor(
    private val wordRepository: WordRepository
) : ViewModel() {

    val searchResults: MutableLiveData<List<WordModel>> by lazy {
        MutableLiveData()
    }

    val selectedWord: MutableLiveData<WordModel> by lazy {
        MutableLiveData()
    }

    fun onSearch(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
        )  {
        viewModelScope.launch(Dispatchers.IO)
        {
            wordRepository.searchWord(s.toString()).collect {
                searchResults.postValue(it)
            }
        }
    }

    fun onItemSelected(position: Int) = searchResults.value?.let {
        selectedWord.postValue(it[position])
    }
}