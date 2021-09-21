package com.nasri.messenger.domain.chat

import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.nasri.messenger.data.firebase.FirebaseConstants.FIRE_CHAT_CONTACT_AVATAR
import com.nasri.messenger.data.firebase.FirebaseConstants.FIRE_CHAT_CONTACT_NAME
import com.nasri.messenger.data.firebase.FirebaseConstants.FIRE_CHAT_CONV_REF
import com.nasri.messenger.data.firebase.FirebaseConstants.FIRE_CHAT_IS_YOU_LAST_SENDER
import com.nasri.messenger.data.firebase.FirebaseConstants.FIRE_CHAT_LAST_MESSAGE_CONTENT
import com.nasri.messenger.data.firebase.FirebaseConstants.FIRE_CHAT_LAST_MESSAGE_SEEN
import com.nasri.messenger.data.firebase.FirebaseConstants.FIRE_CHAT_LAST_MESSAGE_TIME
import com.nasri.messenger.data.firebase.FirebaseConstants.FIRE_CHAT_UNREAD_MESSAGES_COUNT
import com.nasri.messenger.data.firebase.FirebaseConstants.FIRE_COLL_RECENT_CHATS
import com.nasri.messenger.data.firebase.FirebaseConstants.FIRE_COLL_USERS
import com.nasri.messenger.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.TimeUnit

/**
 * Load recent chats from Firebase Firestore
 * */
class LoadRecentChatsUseCase(
    private val db: FirebaseFirestore,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UseCase<String, List<RecentChatModel>>(dispatcher) {

    /**
     * @param userId is the id of the current User
     * */
    override suspend fun execute(userId: String): List<RecentChatModel> {
        if (userId.isBlank()) {
            return emptyList()
        }
        val task = db.collection(FIRE_COLL_USERS)
            .document(userId).collection(FIRE_COLL_RECENT_CHATS).get()
        val snapshot = Tasks.await(task, 20, TimeUnit.MINUTES)

        return snapshot.documents.map { mapDocumentToRecentChatModel(it) }
    }

    private fun mapDocumentToRecentChatModel(document: DocumentSnapshot): RecentChatModel {
        val name = document.getString(FIRE_CHAT_CONTACT_NAME) ?: ""
        val avatarUrl = document.getString(FIRE_CHAT_CONTACT_AVATAR) ?: ""
        val conversationRef = document.getDocumentReference(FIRE_CHAT_CONV_REF)
        val isYouLastSender = document.getBoolean(FIRE_CHAT_IS_YOU_LAST_SENDER) ?: false
        val lastMessageContent = document.getString(FIRE_CHAT_LAST_MESSAGE_CONTENT)
        val lastMessageTimestamp = document.getTimestamp(
            FIRE_CHAT_LAST_MESSAGE_TIME
        ) ?: Timestamp.now()
        val isSeen = document.getBoolean(FIRE_CHAT_LAST_MESSAGE_SEEN) ?: false
        val unreadCount = document.getLong(FIRE_CHAT_UNREAD_MESSAGES_COUNT)

        return RecentChatModel(
            avatarUrl,
            name,
            conversationRef?.id ?: "",
            lastMessageContent,
            lastMessageTimestamp.toDate(),
            isYouLastSender,
            isSeen
        )
    }
}