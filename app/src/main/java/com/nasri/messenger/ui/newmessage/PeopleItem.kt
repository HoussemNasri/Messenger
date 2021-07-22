package com.nasri.messenger.ui.newmessage

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nasri.messenger.R

data class PeopleItem(
    val fullName: String,
    val avatarUri: Uri?,
)

class PeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val fullNameTextView: TextView = itemView.findViewById(R.id.people_full_name)
    private val peopleAvatarImageView: ImageView =
        itemView.findViewById(R.id.people_avatar_image)

    fun bind(peopleItem: PeopleItem) {
        fullNameTextView.text = peopleItem.fullName
        Glide.with(itemView)
            .load(peopleItem.avatarUri)
            .placeholder(R.drawable.ic_avatar)
            .into(peopleAvatarImageView)
    }
}