package com.mertcaliskanyurek.englishwordbox.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.airbnb.lottie.LottieAnimationView
import com.mertcaliskanyurek.englishwordbox.R
import com.mertcaliskanyurek.englishwordbox.databinding.ActivityMainBinding
import com.mertcaliskanyurek.englishwordbox.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main)
        binding.boxAnimationView.setOnClickListener(this::onBoxClick)
    }

    private fun onBoxClick(view: View){
        val boxView = view as LottieAnimationView
        boxView.speed = 5.0f
        boxView.playAnimation()
    }
}