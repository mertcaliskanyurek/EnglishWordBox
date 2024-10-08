package com.mertcaliskanyurek.englishwordbox.ui.view

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mertcaliskanyurek.englishwordbox.R
import com.mertcaliskanyurek.englishwordbox.data.adapter.WordListRecyclerAdapter
import com.mertcaliskanyurek.englishwordbox.data.model.WordModel
import com.mertcaliskanyurek.englishwordbox.data.model.WordState
import com.mertcaliskanyurek.englishwordbox.databinding.ActivityWordListBinding
import com.mertcaliskanyurek.englishwordbox.ui.viewmodel.WordListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WordListActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_WORDS_STATE_TO_BE_LISTED = "EXTRA_WORD_STATE_TO_BE_LISTED"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: WordListViewModel by viewModels()
        val binding: ActivityWordListBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_word_list)
        val wordStateString = intent.getStringExtra(EXTRA_WORDS_STATE_TO_BE_LISTED)
            ?: throw IllegalArgumentException("EXTRA_WORDS_STATE_TO_BE_LISTED should be passed by intent")
        val wordStateToBeListed = WordState.valueOf(wordStateString)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.recyclerWordListView.layoutManager = LinearLayoutManager(this)

        viewModel.words.observe(this) {
            initListView(wordStateToBeListed,viewModel,binding,ArrayList(it),wordStateToBeListed)
        }
        supportActionBar?.title = if(wordStateToBeListed == WordState.IN_BOX) {
            getString(R.string.title_activity_word_box)
        } else {
            getString(R.string.title_activity_trash_box)
        }
        viewModel.prepareWords(wordStateToBeListed)
    }

    private fun initListView(
        wordState: WordState,
        viewModel: WordListViewModel,
        binding: ActivityWordListBinding,
        words: ArrayList<WordModel>,
        wordStateToBeListed: WordState
    ) {
        val adapter = WordListRecyclerAdapter(words)

        viewModel.pictureUrl.observe(this) { picture ->
            if(picture.isNullOrEmpty()) return@observe
            binding.wordCard.setImage(picture)
        }

        adapter.itemClickListener = object : WordListRecyclerAdapter.ItemClickListener {

            override fun onItemClick(word: WordModel, position: Int) {
                binding.wordCard.setWord(word)
                viewModel.prepareWordImage(word.word)
            }

            override fun onOptionButtonClick(word: WordModel, position: Int) {
                viewModel.putBack(word._id)
                adapter.removeItem(position)
                Snackbar.make(binding.root, "Message is deleted", Snackbar.LENGTH_LONG)
                    .setAction("Geri Al") {
                        viewModel.restore(word._id,wordState)
                        adapter.restoreItem(word, position)
                    }.show()
            }

        }

        if(words.size < 1) {
            if(wordStateToBeListed == WordState.IN_BOX) {
                binding.tvNoData.text = getString(R.string.err_no_data_in_box)
            } else {
                binding.tvNoData.text = getString(R.string.err_no_data_in_trash)
            }
            binding.tvNoData.visibility = android.view.View.VISIBLE
        } else {
            binding.tvNoData.visibility = android.view.View.GONE
        }

        binding.recyclerWordListView.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}