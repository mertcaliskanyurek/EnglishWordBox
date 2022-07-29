package com.mertcaliskanyurek.englishwordbox.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.mertcaliskanyurek.englishwordbox.R
import com.mertcaliskanyurek.englishwordbox.databinding.ActivityFirstStartBinding
import com.mertcaliskanyurek.englishwordbox.ui.viewmodel.FirstStartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstStartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: FirstStartViewModel by viewModels()
        val binding: ActivityFirstStartBinding = DataBindingUtil.setContentView(this, R.layout.activity_first_start)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel
        viewModel.progress.observe(this) {
            binding.progressAnimation.setMaxProgress(it)
            binding.progressAnimation.resumeAnimation()

            if(it >= 1.0) startActivity(Intent(this,MainActivity::class.java))
        }
    }
}