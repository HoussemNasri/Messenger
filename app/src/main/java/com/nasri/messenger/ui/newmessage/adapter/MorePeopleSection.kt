package com.nasri.messenger.ui.newmessage.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nasri.messenger.R
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder
import timber.log.Timber

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


    class PeopleViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fullNameTextView: TextView = itemView.findViewById(R.id.people_full_name)
        private val peopleAvatarImageView: ImageView =
            itemView.findViewById(R.id.people_avatar_image)

        fun bind(peopleItem: PeopleItem) {
            fullNameTextView.text = peopleItem.fullName
            Glide.with(itemView)
                .load(peopleItem.avatarUri)
                .into(peopleAvatarImageView)
        }
    }
}