package com.nasri.messenger.ui.auth.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nasri.messenger.domain.registration.signin.SignInUseCase
import com.nasri.messenger.domain.user.SearchUsersUseCase
import com.nasri.messenger.ui.newmessage.NewMessageViewModel
import timber.log.Timber


class SignInViewModelFactory(
    private val signInUseCase: SignInUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            Timber.d("Create new SignInViewModel instance")
            return SignInViewModel(signInUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
