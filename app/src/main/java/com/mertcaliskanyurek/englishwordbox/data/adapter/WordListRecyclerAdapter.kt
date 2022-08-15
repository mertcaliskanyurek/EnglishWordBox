package com.mertcaliskanyurek.englishwordbox.data.adapter

import android.R.attr.data
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mertcaliskanyurek.englishwordbox.R
import com.mertcaliskanyurek.englishwordbox.data.model.WordModel
import com.mertcaliskanyurek.englishwordbox.databinding.WordListViewBinding


class WordListRecyclerAdapter(private val items: ArrayList<WordModel>) :
    RecyclerView.Adapter<WordListRecyclerAdapter.WordViewHolder>() {

    var itemClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view: WordListViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.word_list_view,parent,false)
        return WordViewHolder(view, itemClickListener)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(items[position],position)
    }

    class WordViewHolder(private val binding: WordListViewBinding,
                         private val itemClickListener: ItemClickListener?)
        : RecyclerView.ViewHolder(binding.root) {

        private var pos = 0

        init {
            binding.root.setOnClickListener {
                binding.word?.let {
                    itemClickListener?.onItemClick(it,pos)
                }
            }

            binding.ivOptionButton.setOnClickListener {
                binding.word?.let {
                    itemClickListener?.onOptionButtonClick(it, pos)
                }
            }
        }

        /* Bind flower name and image. */
        fun bind(word: WordModel, position: Int) {
            binding.word = word
            pos = position
        }
    } //ViewHolder

    override fun getItemCount(): Int = items.size

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun restoreItem(item: WordModel, position: Int) {
        items.add(position, item)
        notifyItemInserted(position)
    }

    interface ItemClickListener {
        fun onItemClick(word: WordModel, position: Int)
        fun onOptionButtonClick(word: WordModel, position: Int)
    }
}