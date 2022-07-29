package com.mertcaliskanyurek.englishwordbox.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mertcaliskanyurek.englishwordbox.R
import com.mertcaliskanyurek.englishwordbox.databinding.ActivityMainBinding
import com.mertcaliskanyurek.englishwordbox.ui.viewmodel.MainViewModel
import com.mertcaliskanyurek.englishwordbox.util.AppSettings
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        if (AppSettings.getFirstTime(this)) {
            startActivity(Intent(this,FirstStartActivity::class.java))
            return
        }

        val mainViewModel: MainViewModel by viewModels()

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = mainViewModel
        binding.searchTextField.threshold = 1
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.select_dialog_item, arrayListOf(""))
        binding.searchTextField.setAdapter(adapter)
        binding.searchTextField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                mainViewModel.onItemSelected(p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        mainViewModel.searchResults.observe(this) { words ->
            val resultStrings = words.map { it.word }
            adapter.clear()
            adapter.addAll(resultStrings)
            adapter.notifyDataSetChanged()
        }
    }

    fun reset(v:View) {
        startActivity(Intent(this,FirstStartActivity::class.java))
    }

}