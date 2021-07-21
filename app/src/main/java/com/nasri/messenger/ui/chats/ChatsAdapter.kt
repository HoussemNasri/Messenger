package com.nasri.messenger.ui.chats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
        private val contactAvatarImageView =
            itemView.findViewById<ImageView>(R.id.contact_avatar_image_view)
        private val contactNameTextView =
            itemView.findViewById<TextView>(R.id.contact_name_text_view)
        private val messageDeliveryDateTextView =
            itemView.findViewById<TextView>(R.id.contact_last_message_date_text_view)
        private val unreadMessagesCountTextView =
            itemView.findViewById<TextView>(R.id.contact_unred_messages_count_text_view)

        fun bind(chatItemModel: RecentChatModel) {
            bindContactName(chatItemModel.contactName)
            bindUnreadMessagesCount(chatItemModel.conversationUnreadMessagesCount)
            bindMessageDeliveryDate(chatItemModel.conversationLastMessageDate.time)
            bindContactAvatarImage(chatItemModel.contactAvatarImageUri)
        }

        private fun bindContactName(name: String) {
            if (name.isBlank()) {
                contactNameTextView.text =
                    itemView.context.getString(R.string.anonymous_contact_name)
            } else {
                contactNameTextView.text = name
            }
        }

        private fun bindUnreadMessagesCount(count: Int) {
            if (count == 0) {
                unreadMessagesCountTextView.visibility = View.INVISIBLE
            } else {
                unreadMessagesCountTextView.visibility = View.VISIBLE
                unreadMessagesCountTextView.text = count.toString()
            }
        }


        private fun bindMessageDeliveryDate(timestamp: Long) {
            messageDeliveryDateTextView.text =
                MessengerDateFormatter.formatMessageDeliveryDate(timestamp)
        }

        fun bindContactAvatarImage(imageUri: String) {
            Glide.with(itemView)
                .load(imageUri)
                .placeholder(R.drawable.ic_avatar)
                .into(contactAvatarImageView)
        }
    }


}