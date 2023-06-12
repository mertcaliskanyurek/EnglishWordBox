package com.mertcaliskanyurek.englishwordbox.ui.view

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import com.mertcaliskanyurek.englishwordbox.R
import com.mertcaliskanyurek.englishwordbox.databinding.SettingsActivityBinding

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: SettingsActivityBinding = DataBindingUtil.setContentView(this,R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.settings, SettingsFragment())
                    .commit()
        }
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.title_activity_settings)
        }
        initViews(binding)
    }

    private fun initViews(binding: SettingsActivityBinding){
        binding.tvAbout.movementMethod = LinkMovementMethod.getInstance()
        binding.btnChangeLanguage.setOnClickListener {
            startActivity(Intent(this,SelectMotherTongueActivity::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            val reminderPref = findPreference<SeekBarPreference>("reminder")
            reminderPref?.let {
                it.summary = getString(R.string.time_period,it.value)
                it.onPreferenceChangeListener =
                    Preference.OnPreferenceChangeListener { preference, newValue ->
                        preference.summary = getString(R.string.time_period,newValue as Int)
                        true
                    }
            }
        }
    }
}