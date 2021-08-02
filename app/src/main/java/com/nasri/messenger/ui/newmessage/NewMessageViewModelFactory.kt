package com.nasri.messenger.ui.newmessage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nasri.messenger.domain.user.UserSearchUseCase
import timber.log.Timber

class NewMessageViewModelFactory(
    private val userId: String,
    private val userSearchUseCase: UserSearchUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewMessageViewModel::class.java)) {
            Timber.d("Create new NewMessageViewModel instance")
            return NewMessageViewModel(userId, userSearchUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}