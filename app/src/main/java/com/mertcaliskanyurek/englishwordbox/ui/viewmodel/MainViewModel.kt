package com.mertcaliskanyurek.englishwordbox.ui.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mertcaliskanyurek.englishwordbox.data.model.WordLevel
import com.mertcaliskanyurek.englishwordbox.data.model.WordModel
import com.mertcaliskanyurek.englishwordbox.data.repository.IWordRepository
import com.mertcaliskanyurek.englishwordbox.data.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Mert Çalışkanyürek on 08,December,2021
 */

@HiltViewModel
class MainViewModel @Inject constructor(
    private val wordRepository: WordRepository
) : ViewModel() {

    fun getWordsByLevel(level: WordLevel): LiveData<List<WordModel>> =
        wordRepository.getWordsByLevel(level)
}