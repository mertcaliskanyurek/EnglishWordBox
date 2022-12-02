package com.mertcaliskanyurek.englishwordbox.ui.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.mertcaliskanyurek.englishwordbox.R
import com.mertcaliskanyurek.englishwordbox.data.adapter.WordListArrayAdapter
import com.mertcaliskanyurek.englishwordbox.data.model.WordModel
import com.mertcaliskanyurek.englishwordbox.data.model.WordState
import com.mertcaliskanyurek.englishwordbox.databinding.ActivityMainBinding
import com.mertcaliskanyurek.englishwordbox.ui.viewmodel.MainViewModel
import com.mertcaliskanyurek.englishwordbox.util.AppSettings
import com.mertcaliskanyurek.englishwordbox.util.ReminderUtil
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_WORD = "EXTRA_WORD"
        private const val NOTIFICATION_PERMISSION_CODE = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        if (AppSettings.getFirstTime(this)) {
            startActivity(Intent(this,SelectMotherTongueActivity::class.java))
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        } else {
            ReminderUtil.initNextReminder(this)
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
                binding.wordCard.setImage(it)
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

        binding.settingsButton.setOnClickListener {
            startActivity(Intent(this,SettingsActivity::class.java))
        }
        intent.getStringExtra(EXTRA_WORD)?.let {
            viewModel.onSearch(it,0,0,it.length)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            ReminderUtil.initNextReminder(this)
        }

        requestPermissions(
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            NOTIFICATION_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Checking the request code of our request
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {

            // If permission is granted
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Displaying a toast
                ReminderUtil.initNextReminder(this)
            } else {
                // Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG)
                    .show()
            }
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