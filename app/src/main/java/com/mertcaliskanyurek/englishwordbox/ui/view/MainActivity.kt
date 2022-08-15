package com.mertcaliskanyurek.englishwordbox.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mertcaliskanyurek.englishwordbox.R
import com.mertcaliskanyurek.englishwordbox.data.adapter.WordListArrayAdapter
import com.mertcaliskanyurek.englishwordbox.data.model.WordModel
import com.mertcaliskanyurek.englishwordbox.data.model.WordState
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
            startActivity(Intent(this,SelectMotherTongueActivity::class.java))
            return
        }

        val viewModel: MainViewModel by viewModels()
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val adapter: ArrayAdapter<WordModel> = WordListArrayAdapter(this, arrayListOf<WordModel>()) {
            viewModel.onItemClick(it)
            binding.searchTextField.clearFocus()
        }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.searchTextField.threshold = 1
        binding.searchTextField.setAdapter(adapter)

        viewModel.searchResults.observe(this) { words ->
            adapter.clear()
            adapter.addAll(words)
            adapter.notifyDataSetChanged()
        }

        viewModel.selectedWord.observe(this) { word ->
            hideKeyboard()
            binding.wordCard.setWord(word)
        }

        viewModel.pictureUrl.observe(this) { picture ->
            picture?.let {
                binding.wordCard.setImage(picture)
            }
        }

        binding.wordCard.setOptionsVisible(true)

        binding.wordCard.trashButton.setOnClickListener() {
            viewModel.onTrash()
            binding.trashAnimation.playAnimation()
            binding.wordCard.hideWithAnim(false)
        }

        binding.wordCard.boxButton.setOnClickListener() {
            viewModel.onBox()
            binding.boxAnimation.playAnimation()
            binding.wordCard.hideWithAnim(true)
        }

        binding.wordCard.soundButton.setOnClickListener(viewModel::onSoundClick)

        binding.boxAnimation.setOnClickListener {
            startWordListActivity(WordState.IN_BOX)
        }

        binding.trashAnimation.setOnClickListener {
            startWordListActivity(WordState.IN_TRASH)
        }
    }

    private fun startWordListActivity(state: WordState) {
        val intent = Intent(this,WordListActivity::class.java)
        intent.putExtra(WordListActivity.EXTRA_WORDS_STATE_TO_BE_LISTED,state.name)
        startActivity(intent)
    }

    private fun hideKeyboard() = this.currentFocus?.let { view ->
        val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

}