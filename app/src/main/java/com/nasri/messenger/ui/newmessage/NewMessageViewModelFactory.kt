package com.nasri.messenger.ui.newmessage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nasri.messenger.domain.user.SearchUsersUseCase
import timber.log.Timber

class NewMessageViewModelFactory(
    private val userId: String,
    private val searchUsersUseCase: SearchUsersUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewMessageViewModel::class.java)) {
            Timber.d("Create new NewMessageViewModel instance")
            return NewMessageViewModel(userId, searchUsersUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}