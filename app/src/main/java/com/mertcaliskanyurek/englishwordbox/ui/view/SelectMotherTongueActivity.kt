package com.mertcaliskanyurek.englishwordbox.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.mertcaliskanyurek.englishwordbox.R
import com.mertcaliskanyurek.englishwordbox.databinding.ActivitySelectMotherTongueBinding
import com.mertcaliskanyurek.englishwordbox.ui.viewmodel.SelectMotherTongueViewModel
import com.mertcaliskanyurek.englishwordbox.util.AppSettings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectMotherTongueActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: SelectMotherTongueViewModel by viewModels()
        val binding: ActivitySelectMotherTongueBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_mother_tongue)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.progress.observe(this) {
            binding.progressAnimation.setMaxProgress(it)
            binding.progressAnimation.resumeAnimation()

            if(it >= 1.0) {
                AppSettings.setFirstTime(this,false)
                startActivity(Intent(this,MainActivity::class.java))
            }
        }
    }
}