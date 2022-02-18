package com.mertcaliskanyurek.englishwordbox.ui.view

import android.animation.Animator
import android.animation.ObjectAnimator
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
import android.view.animation.Animation

import android.view.animation.ScaleAnimation




@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        prepareWords()

        mainViewModel.currentWord.observe(this) {
            binding.currentWord = it
        }
        mainViewModel.currentProgress.observe(this) {
            binding.progressAnimationView.setMaxProgress((it / 100).toFloat())
            binding.progressAnimationView.resumeAnimation()
        }
        mainViewModel.openTranslation.observe(this) {
            binding.openTranslation = it
        }

        mainViewModel.currentProgress.observe(this) {
            binding.currentProgress = (it * 100).toInt()
        }

        binding.boxAnimationView.setOnClickListener(this::onBoxClick)
        binding.boxAnimationView.speed = 5.0f
        binding.boxAnimationView.repeatCount = 1
        binding.boxAnimationView.repeatMode = LottieDrawable.REVERSE

        binding.soundAnimationView.setOnClickListener(this::onSoundClick)
        binding.soundAnimationView.speed = 5.0f
        binding.soundAnimationView.repeatCount = 1
        binding.soundAnimationView.repeatMode = LottieDrawable.REVERSE

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
        boxView.playAnimation()
        mainViewModel.pickWord(WordLevel.A1)

        cardAnimation()

    }

    private fun onSoundClick(view: View) {
        val soundView = view as LottieAnimationView
        soundView.repeatCount = 1
        soundView.repeatMode = LottieDrawable.REVERSE
        soundView.speed = 0.5f
        soundView.playAnimation()
    }

    private fun cardAnimation() {
        val anim: Animation = ScaleAnimation(
            0f, 1f,  // Start and end values for the X axis scaling
            0f, 1f,  // Start and end values for the Y axis scaling
            Animation.RELATIVE_TO_SELF, 0.5f,  // Pivot point of X scaling
            Animation.RELATIVE_TO_SELF, 0.5f
        ) // Pivot point of Y scaling

        anim.fillAfter = true // Needed to keep the result of the animation

        anim.duration = 1000
        binding.wordCard.startAnimation(anim)
    }

}