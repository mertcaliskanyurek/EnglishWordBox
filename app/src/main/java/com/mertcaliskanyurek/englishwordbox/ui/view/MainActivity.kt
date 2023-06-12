package com.mertcaliskanyurek.englishwordbox.ui.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.mertcaliskanyurek.englishwordbox.R
import com.mertcaliskanyurek.englishwordbox.data.adapter.WordListArrayAdapter
import com.mertcaliskanyurek.englishwordbox.data.model.WordState
import com.mertcaliskanyurek.englishwordbox.databinding.ActivityMainBinding
import com.mertcaliskanyurek.englishwordbox.ui.viewmodel.MainViewModel
import com.mertcaliskanyurek.englishwordbox.util.AppConstants
import com.mertcaliskanyurek.englishwordbox.util.AppSettings
import com.mertcaliskanyurek.englishwordbox.util.ReminderUtil
import com.mertcaliskanyurek.englishwordbox.util.hideSoftInput
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_WORD = "EXTRA_WORD"
        private const val NOTIFICATION_PERMISSION_CODE = 200
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        if (AppSettings.getFirstTime(this)) {
            startActivity(Intent(this,SelectMotherTongueActivity::class.java))
            return
        }
        setLocale(AppSettings.getMotherTongue(this))

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        } else {
            ReminderUtil.initNextReminder(this)
        }

        val viewModel: MainViewModel by viewModels()

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.searchTextField.threshold = 1

        viewModel.apply {
            searchResults.observe(this@MainActivity) { words ->
                val adapter = WordListArrayAdapter(this@MainActivity, words) {
                    viewModel.onItemClick(it)
                    binding.searchTextField.clearFocus()
                    binding.searchTextField.hideSoftInput()
                }
                binding.searchTextField.setAdapter(adapter)
            }

            selectedWord.observe(this@MainActivity) { word ->
                binding.filledTextField.visibility = View.GONE
                binding.wordCard.setWord(word)
            }

            pictureUrl.observe(this@MainActivity) { picture ->
                binding.wordCard.setImage(picture)
            }

            error.observe(this@MainActivity) { error->
                error?.let { err->
                    when(err) {
                        AppConstants.ErrorType.ERROR_WORD_NOT_FOUND -> {
                            val word = binding.searchTextField.text.toString()
                            showWordNotFoundDialog(word) {
                                viewModel.sendUnknownWordReport(word)
                            }
                        }
                        AppConstants.ErrorType.ERROR_FEEDBACK_NOT_SENT -> {
                            Toast.makeText(this@MainActivity, R.string.report_failure, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        binding.apply {
            wordCard.apply {
                setOnTrashButtonClick {
                    viewModel.onTrash()
                    wordCard.hideWithAnim(false)
                    trashAnimation.playAnimation()
                }

                setOnBoxButtonClick {
                    viewModel.onBox()
                    wordCard.hideWithAnim(true)
                    boxAnimation.playAnimation()
                }

                setOnReportButtonClick {
                    showReportDialog {
                        viewModel.onReportClick(it)
                    }
                }

                setOnSoundButtonClick(viewModel::onSoundClick)
                onDismiss = { filledTextField.visibility = View.VISIBLE }
            }

            mainLayout.setOnTouchListener { v, _ ->
                searchTextField.clearFocus()
                v.hideSoftInput()
                false
            }

            filledTextField.setEndIconOnClickListener {
                it.clearFocus()
                it.hideSoftInput()
                viewModel.getWord(binding.searchTextField.text.toString().lowercase())
            }

            searchTextField.setOnEditorActionListener { v, actionId, event ->
                if ((event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_SEARCH)) {
                    viewModel.getWord(v.text.toString())
                    v.clearFocus()
                    v.hideSoftInput()
                    true
                } else false
            }

            ivBack.setOnClickListener {
                onBackPressed()
            }

            boxAnimation.setOnClickListener {
                startWordListActivity(WordState.IN_BOX)
            }

            trashAnimation.setOnClickListener {
                startWordListActivity(WordState.IN_TRASH)
            }

            settingsButton.setOnClickListener {
                startActivity(Intent(this@MainActivity,SettingsActivity::class.java))
            }

        }

        intent.getStringExtra(EXTRA_WORD)?.let {
            viewModel.getWord(it)
        }

    }

    private fun showReportDialog(onChoiceListener: (String)->Unit) {
        val dialog = AlertDialog.Builder(this)
        val reasons = resources.getStringArray(R.array.report_reasons)
        dialog.setTitle(R.string.report_title)
        dialog.setIcon(R.drawable.ic_baseline_report)
        dialog.setSingleChoiceItems(reasons,-1) { dialog, which ->
            onChoiceListener(reasons[which])
            dialog.dismiss()
            Toast.makeText(baseContext,R.string.report_success,Toast.LENGTH_SHORT).show()
        }
        dialog.setNegativeButton(R.string.common_cancel){
                dialog, which -> dialog.dismiss()
        }
        dialog.show()
    }

    private fun showWordNotFoundDialog(word: String, onSendClick: () -> Unit) {
        val dialog = AlertDialog.Builder(this)
        dialog.setCancelable(true)
        dialog.setIcon(R.drawable.baseline_question_mark_24)
        dialog.setTitle(R.string.word_not_found_title)
        dialog.setMessage(getString(R.string.word_not_found_message, word))
        dialog.setPositiveButton(R.string.common_send) { dialog, _ ->
            onSendClick()
            dialog.dismiss()
        }
        dialog.setNegativeButton(R.string.common_cancel) { dialog, _ ->
            dialog.dismiss()
        }
        dialog.show()
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
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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

    override fun onBackPressed() {
        if (binding.wordCard.cardOpened) {
            binding.wordCard.close()
        } else {
            super.onBackPressed()
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