package com.nasri.messenger.data.firebase

import com.nasri.messenger.data.user.UserData

class FirebaseConstants {
    companion object {
        const val FIRE_PHOTO_URL = "photoUrl"
        const val FIRE_USERNAME = "username"
        const val FIRE_LAST_SIGN_IN = "lastSignIn"
        const val FIRE_ACCOUNT_CREATION = "CreatedAt"
        const val FIRE_PHONE = "phoneNUmber"
        const val FIRE_EMAIL = "email"
        const val FIRE_COLL_CONTACTS = "contacts"
        const val FIRE_COLL_RECENT_CHATS = "recentChats"
        const val FIRE_COLL_USERS = "users"
        const val FIRE_CHAT_CONTACT_AVATAR = "contactAvatarImage"
        const val FIRE_CHAT_CONTACT_NAME = "contactName"
        const val FIRE_CHAT_CONV_REF = "conversationReference"
        const val FIRE_CHAT_IS_YOU_LAST_SENDER = "isYouLastSender"
        const val FIRE_CHAT_LAST_MESSAGE_CONTENT = "lastMessageContent"
        const val FIRE_CHAT_LAST_MESSAGE_SEEN = "lastMessageSeen"
        const val FIRE_CHAT_UNREAD_MESSAGES_COUNT = "unreadMessagesCount"
        const val FIRE_CHAT_LAST_MESSAGE_TIME = "lastMessageTimestamp"

    }
}