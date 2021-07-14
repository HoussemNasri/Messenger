package com.nasri.messenger.ui.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nasri.messenger.domain.chat.LoadRecentChatsUseCase

class ChatsViewModelFactory(
    private val loadRecentChatsUseCase: LoadRecentChatsUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatsViewModel::class.java)) {
            return ChatsViewModel(loadRecentChatsUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}