package com.nasri.messenger.ui.newmessage

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.nasri.messenger.R
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder

/**
 * Share common logic for creating sections
 *
 * @param T is the type of data model
 * */
abstract class BaseSection<T>(
    @LayoutRes private val itemResourceId: Int,
    @StringRes private val headerTitleStringId: Int = R.string.section_header_header,
    @LayoutRes private val headerLayoutId: Int = R.layout.section_header,
    @LayoutRes private val loadingLayoutId: Int = R.layout.section_header,
    @LayoutRes private val failedLayoutId: Int = R.layout.section_header,
) : Section(
    SectionParameters.builder()
        .itemResourceId(itemResourceId)
        .headerResourceId(headerLayoutId)
        .build()
) {
    internal var dataItems: List<T> = listOf()

    final override fun getContentItemsTotal(): Int = dataItems.size

    override fun getHeaderViewHolder(view: View?): RecyclerView.ViewHolder = EmptyViewHolder(view)

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?) {
        val itemView = (holder as EmptyViewHolder).itemView
        val headerTextView = holder.itemView.findViewById<TextView>(R.id.header)
        headerTextView.text = itemView.context.getString(headerTitleStringId)
    }

    fun setData(dataItems: List<T>) {
        this.dataItems = dataItems
    }
}