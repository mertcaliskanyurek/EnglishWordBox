package com.mertcaliskanyurek.englishwordbox.data.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.mertcaliskanyurek.englishwordbox.data.model.WordModel


class WordListArrayAdapter(activity: Activity, private val items: ArrayList<WordModel>,
                           private val onClick: (WordModel) -> Unit) :
    ArrayAdapter<WordModel>(activity,0,items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        if (view == null) {
            view = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.select_dialog_item, parent, false)
        }
        val word: WordModel? = getItem(position)
        if (word != null) {
            (view as TextView).text = word.word
            view.setOnClickListener { onClick(word) }
        }
        return view!!
    }

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val res = FilterResults()
            res.values = items
            res.count = items.size
            return res
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            notifyDataSetChanged()
        }
    }

}