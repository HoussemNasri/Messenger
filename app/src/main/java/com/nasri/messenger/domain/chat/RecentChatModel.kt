package com.nasri.messenger.domain.chat

import android.net.Uri
import java.util.*

/**
 * Describes an item in the Chats, to display recent Chats that User had.
 * each item contains contact's image and name, with basic conversation info
 * */
data class RecentChatModel(
    val contactAvatarImageUri: String,
    val contactName: String,
    val conversationReference: String,
    val conversationLastMessage: String?,
    val conversationLastMessageDate: Date,
    val isYouLastSender: Boolean,
    val isSeen : Boolean,
    val conversationUnreadMessagesCount: Int = 0
)