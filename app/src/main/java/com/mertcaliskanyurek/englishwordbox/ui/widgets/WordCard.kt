package com.mertcaliskanyurek.englishwordbox.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.mertcaliskanyurek.englishwordbox.R
import com.mertcaliskanyurek.englishwordbox.data.model.WordModel
import com.mertcaliskanyurek.englishwordbox.data.model.WordState
import com.mertcaliskanyurek.englishwordbox.databinding.WordCardBinding

class WordCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding: WordCardBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.word_card,this,true)

    init {
        binding.word = null
        binding.iwPicture.visibility = View.GONE
        binding.closeButton.setOnClickListener(){
            binding.word = null
        }
    }

    fun setWord(word: WordModel) {
        binding.word = word
        initOptionButtons(word)
        showWithAnim()
    }

    fun setImage(imageUrl: String) {
        Glide.with(context).load(imageUrl).into(binding.iwPicture)
        binding.iwPicture.visibility = View.VISIBLE
    }

    /*fun setOptionsVisible(isVisible: Boolean) {
        binding.optionsView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }*/

    fun setOnTrashButtonClick(listener: OnClickListener) = binding.trashButton.setOnClickListener(listener)
    fun setOnBoxButtonClick(listener: OnClickListener) = binding.boxButton.setOnClickListener(listener)
    fun setOnReportButtonClick(listener: OnClickListener) = binding.reportButton.setOnClickListener(listener)
    fun setOnSoundButtonClick(listener: OnClickListener) = binding.soundAnimation.setOnClickListener(listener)

    private fun showWithAnim() {
        //reset animation
        translationY = 0f
        translationX = 0f
        scaleX = 1.0f
        scaleY = 1.0f
        alpha = 1.0f
        startAnimation(AnimationUtils.loadAnimation(context,R.anim.bounce))
    }

    private fun initOptionButtons(word: WordModel){
        when(word.state) {
            WordState.IN_BOX -> binding.boxButton.visibility = View.GONE
            WordState.IN_TRASH -> binding.boxButton.visibility = View.GONE
            WordState.NOTHING -> {}
        }
    }

    fun hideWithAnim(toBox: Boolean) = animate()
        .scaleX(0.1f).scaleY(0.1f)
        .alpha(0.1f)
        .translationY(height.toFloat())
        .translationX(if(toBox) width.toFloat() else -width.toFloat())
        .setDuration(300)
        .withEndAction {
            binding.word = null
        }.start()

}