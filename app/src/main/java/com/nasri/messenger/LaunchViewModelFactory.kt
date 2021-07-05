package com.nasri.messenger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nasri.messenger.domain.prefs.UserLoggedInUseCase
import timber.log.Timber

class LaunchViewModelFactory constructor(
    val userLoggedInUseCase: UserLoggedInUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LaunchViewModel::class.java)) {
            Timber.d("Create new LaunchViewModel instance")
            return LaunchViewModel(userLoggedInUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}