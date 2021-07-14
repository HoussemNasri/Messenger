package com.nasri.messenger.ui.chats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nasri.messenger.R
import com.nasri.messenger.domain.chat.RecentChatModel

class ChatsAdapter(
    private var chatList: List<RecentChatModel>
) : RecyclerView.Adapter<ChatsAdapter.ChatItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatItemViewHolder {
        return ChatItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatItemViewHolder, position: Int) {
        holder.bind(chatList[position])
    }

    override fun getItemCount(): Int = chatList.size

    fun setChats(recentChatModel: List<RecentChatModel>) {
        chatList = recentChatModel
        notifyDataSetChanged()
    }

    class ChatItemViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val avatar = itemView.findViewById<ImageView>(R.id.contact_avatar_image_view)
        val name = itemView.findViewById<TextView>(R.id.contact_name_text_view)
        val deliveryDate = itemView.findViewById<TextView>(R.id.contact_last_message_date_text_view)
        val unreadCount =
            itemView.findViewById<TextView>(R.id.contact_unred_messages_count_text_view)

        fun bind(chatItemModel: RecentChatModel) {
            name.text =
                if (chatItemModel.contactName.isBlank()) "Anonymous" else chatItemModel.contactName
            if (chatItemModel.conversationUnreadMessagesCount == 0) {
                unreadCount.visibility = View.INVISIBLE
            } else {
                unreadCount.visibility = View.VISIBLE
            }
        }
    }


}