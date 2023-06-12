package com.mertcaliskanyurek.englishwordbox.data.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.mertcaliskanyurek.englishwordbox.data.model.WordModel


class WordListArrayAdapter(activity: Activity, items: List<WordModel>,
                           private val onClick: (WordModel) -> Unit) :
    ArrayAdapter<WordModel>(activity,0,items) {

    private val items: List<WordModel> = ArrayList(items)

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
            val filteredResults = items.filter { it.word.contains(p0.toString(), true) }
            res.values = filteredResults
            res.count = filteredResults.size
            return res
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            p1?.values?.let {
                if(it is List<*>) {
                    clear()
                    addAll(it as List<WordModel>)
                    notifyDataSetChanged()
                }
            }
        }
    }

}