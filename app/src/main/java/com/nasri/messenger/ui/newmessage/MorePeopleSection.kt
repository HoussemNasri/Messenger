package com.nasri.messenger.ui.newmessage

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nasri.messenger.R
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder

class MorePeopleSection(
    private var dataItems: List<PeopleItem>
) : Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.people_item)
        .headerResourceId(R.layout.section_header)
        .build()
) {
    override fun getContentItemsTotal(): Int {
        return dataItems.size
    }

    override fun getItemViewHolder(view: View?): RecyclerView.ViewHolder = PeopleViewHolder(view!!)

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder?, position: Int) =
        (holder as PeopleViewHolder).bind(dataItems[position])

    override fun getHeaderViewHolder(view: View?): RecyclerView.ViewHolder = EmptyViewHolder(view)

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?) {
        val itemView = (holder as EmptyViewHolder).itemView
        val headerTextView = holder.itemView.findViewById<TextView>(R.id.header)
        headerTextView.text = itemView.context.getString(R.string.more_people_header)
    }


}