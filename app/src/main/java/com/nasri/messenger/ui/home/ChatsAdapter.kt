package com.nasri.messenger.ui.home

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nasri.messenger.domain.home.ChatItemModel

class ChatsAdapter(
    private val chatList: List<ChatItemModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int = chatList.size

    class ChatItemViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView)


}