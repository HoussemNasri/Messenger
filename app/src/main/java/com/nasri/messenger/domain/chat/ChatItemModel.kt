package com.nasri.messenger.domain.chat

import android.net.Uri
import java.util.*

data class ChatItemModel(
    val avatarImageUri: Uri,
    val contactName: String,
    val lastMessage: String?,
    val lastMessageDate: Date,
    val unreadMessagesCount: Int = 0
)