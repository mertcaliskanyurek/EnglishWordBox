package com.mertcaliskanyurek.englishwordbox.ui.view

import android.animation.Animator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.mertcaliskanyurek.englishwordbox.R
import com.mertcaliskanyurek.englishwordbox.data.model.WordLevel
import com.mertcaliskanyurek.englishwordbox.data.model.WordModel
import com.mertcaliskanyurek.englishwordbox.databinding.ActivityMainBinding
import com.mertcaliskanyurek.englishwordbox.ui.viewmodel.MainViewModel
import com.mertcaliskanyurek.englishwordbox.util.GsonUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main)

        prepareWords()

        mainViewModel.currentWord.observe(this,{
            binding.currentWord = it
        })
        mainViewModel.currentProgress.observe(this, {
            binding.progressAnimationView.setMaxProgress((it/100).toFloat())
            binding.progressAnimationView.resumeAnimation()
        })
        mainViewModel.openTranslation.observe(this, {
            binding.openTranslation = it
        })

        mainViewModel.currentProgress.observe(this, {
            binding.currentProgress = (it * 100).toInt()
        })

        binding.boxAnimationView.setOnClickListener(this::onBoxClick)
        binding.boxAnimationView.speed = 5.0f

        binding.progressAnimationView.speed = 2.0f
    }

    private fun prepareWords() {
        WordLevel.values().forEach { level ->
            val wordList = GsonUtil.getWords(this,level.name+".json")
            val wordModelList = wordList.map { WordModel(word = it,level = level.name) }
            runBlocking { mainViewModel.addWords(wordModelList) }
        }
    }

    private fun onBoxClick(view: View){
        val boxView = view as LottieAnimationView
        boxView.repeatCount = 1
        boxView.repeatMode = LottieDrawable.REVERSE
        boxView.playAnimation()
        mainViewModel.pickWord(WordLevel.A1)
    }

}