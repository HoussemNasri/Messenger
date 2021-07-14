package com.nasri.messenger.ui.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import com.nasri.messenger.domain.chat.LoadRecentChatsUseCase
import com.nasri.messenger.domain.result.data
import com.nasri.messenger.domain.result.succeeded

class ChatsViewModel(
    private val loadRecentChatsUseCase: LoadRecentChatsUseCase
) : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    val recentChats = liveData {
        val result = loadRecentChatsUseCase(auth.currentUser?.uid ?: "")
        if (result.succeeded) {
            emit(result.data)
        }
    }
}