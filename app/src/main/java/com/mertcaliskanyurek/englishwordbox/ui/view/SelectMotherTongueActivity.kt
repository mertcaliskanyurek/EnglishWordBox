package com.mertcaliskanyurek.englishwordbox.ui.view

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mertcaliskanyurek.englishwordbox.R
import com.mertcaliskanyurek.englishwordbox.databinding.ActivitySelectMotherTongueBinding
import com.mertcaliskanyurek.englishwordbox.ui.viewmodel.SelectMotherTongueViewModel
import com.mertcaliskanyurek.englishwordbox.util.AppSettings
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class SelectMotherTongueActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: SelectMotherTongueViewModel by viewModels()
        val binding: ActivitySelectMotherTongueBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_mother_tongue)
        supportActionBar?.hide()
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.progress.observe(this) {
            binding.progressAnimation.setMaxProgress(it)
            binding.progressAnimation.resumeAnimation()

            if(it >= 1.0) {
                AppSettings.setFirstTime(this,false)
                startActivity(Intent(this,MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                })
            }
        }

        viewModel.selected.observe(this) {
            if(it) {
                setLocale(viewModel.selectedTounge.split('-')[1])
            }
        }
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources = resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        AppSettings.setMotherTongue(this, languageCode)
    }
}