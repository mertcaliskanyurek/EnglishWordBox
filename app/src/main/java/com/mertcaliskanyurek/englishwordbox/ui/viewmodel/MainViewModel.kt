package com.mertcaliskanyurek.englishwordbox.ui.viewmodel
import androidx.lifecycle.*
import com.mertcaliskanyurek.englishwordbox.data.model.WordLevel
import com.mertcaliskanyurek.englishwordbox.data.model.WordModel
import com.mertcaliskanyurek.englishwordbox.data.repository.WordRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Mert Çalışkanyürek on 08,December,2021
 */

@HiltViewModel
class MainViewModel @Inject constructor(
    private val wordRepository: WordRepository
) : ViewModel() {

    var currentWord: MutableLiveData<WordModel> = MutableLiveData()

    var currentProgress: MutableLiveData<Int> = MutableLiveData()
    var openTranslation: MutableLiveData<Boolean> = MutableLiveData()

    var wordPicked: MutableLiveData<Boolean> = MutableLiveData()

    init {
        viewModelScope.launch {
             wordRepository.getWord(WordLevel.A1).collect {
             currentWord.value = it
            }

            currentProgress.value = wordRepository.getProgress(WordLevel.A1)
            openTranslation.value = false
        }

    }

    suspend fun addWords(wordList: List<WordModel>) = wordRepository.insertAll(wordList)

    fun pickWord(level: WordLevel) {
        viewModelScope.launch {
            wordRepository.getWord(WordLevel.A1).collect {
                currentWord.value = it

            }
            wordPicked.value = true
            openTranslation.value = false
            currentProgress.value = wordRepository.getProgress(level)
        }
    }

    fun answerSubmitted(answer: String) {
        openTranslation.value = true
    }

    suspend fun wordKnown(word: WordModel) {
        word.knowCount = word.knowCount + 1
        wordRepository.updateWord(word)
    }

    suspend fun uselessWord(word: WordModel) {
        word.useless = true
        wordRepository.updateWord(word)
    }
}