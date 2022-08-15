package com.mertcaliskanyurek.englishwordbox.ui.view

import android.os.Bundle
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
            initListView(wordStateToBeListed,viewModel,binding,ArrayList(it))
        }
        viewModel.prepareWords(wordStateToBeListed)
    }

    private fun initListView(wordState: WordState,
                             viewModel: WordListViewModel,
                             binding: ActivityWordListBinding,
                             words: ArrayList<WordModel>) {
        val adapter = WordListRecyclerAdapter(words)

        viewModel.pictureUrl.observe(this) { picture ->
            picture?.let {
                binding.wordCard.setImage(picture)
            }
        }

        adapter.itemClickListener = object : WordListRecyclerAdapter.ItemClickListener {

            override fun onItemClick(word: WordModel, position: Int) {
                binding.wordCard.setWord(word)
                binding.wordCard.setOptionsVisible(false)
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

        binding.recyclerWordListView.adapter = adapter
    }

}