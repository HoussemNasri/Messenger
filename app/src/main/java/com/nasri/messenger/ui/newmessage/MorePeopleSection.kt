package com.nasri.messenger.ui.newmessage

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nasri.messenger.R
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder

class MorePeopleSection : BaseSection<PeopleItem>(R.layout.people_item, R.string.more_people_header) {

    override fun getItemViewHolder(view: View?): RecyclerView.ViewHolder = PeopleViewHolder(view!!)

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder?, position: Int) =
        (holder as PeopleViewHolder).bind(dataItems[position])

}