package com.mertcaliskanyurek.englishwordbox.ui.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airbnb.lottie.LottieAnimationView
import com.mertcaliskanyurek.englishwordbox.data.model.WordModel
import com.mertcaliskanyurek.englishwordbox.data.model.WordState
import com.mertcaliskanyurek.englishwordbox.data.network.WordBoxApi
import com.mertcaliskanyurek.englishwordbox.data.repository.WordRepository
import com.mertcaliskanyurek.englishwordbox.helper.SoundPlayer
import com.mertcaliskanyurek.englishwordbox.util.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Mert Çalışkanyürek on 08,December,2021
 */

@HiltViewModel
class MainViewModel @Inject constructor(
    private val wordRepository: WordRepository,
    private val soundPlayer: SoundPlayer,
    private val wordBoxApi: WordBoxApi
) : ViewModel() {

    val searchResults: MutableLiveData<List<WordModel>> by lazy {
        MutableLiveData()
    }

    val selectedWord: MutableLiveData<WordModel> by lazy {
        MutableLiveData()
    }

    val pictureUrl: MutableLiveData<String> by lazy {
        MutableLiveData("")
    }

    //var onBox: () -> Unit = {}
    //var onTrash: () -> Unit = {}

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

    fun onItemClick(word: WordModel) {
        selectedWord.postValue(word)
        soundPlayer.prepare(word.sound)
        viewModelScope.launch(Dispatchers.IO) {
            val res = wordBoxApi.picture(word.word,AppConstants.API_KEY)
            if(res.isSuccessful) pictureUrl.postValue(res.body())
        }
    }

    fun onSoundClick(view: View) {
        val animView = view as LottieAnimationView
        animView.playAnimation()
        soundPlayer.playSound()
    }

    fun onTrash() = selectedWord.value?.let {
            viewModelScope.launch(Dispatchers.IO) {
                wordRepository.updateState(it._id,WordState.IN_TRASH)
            }
        }


    fun onBox() = selectedWord.value?.let {
            viewModelScope.launch(Dispatchers.IO) {
                wordRepository.updateState(it._id, WordState.IN_BOX)
            }
    }
}
