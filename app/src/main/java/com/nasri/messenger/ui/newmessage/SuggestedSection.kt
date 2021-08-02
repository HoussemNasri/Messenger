package com.nasri.messenger.ui.newmessage

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.nasri.messenger.R
import com.nasri.messenger.ui.base.BaseSection

class SuggestedSection : BaseSection<PeopleItem>(R.layout.people_item, R.string.suggested_header) {


    override fun getItemViewHolder(view: View?): RecyclerView.ViewHolder = PeopleViewHolder(view!!)

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder?, position: Int) =
        (holder as PeopleViewHolder).bind(dataItems[position])

}