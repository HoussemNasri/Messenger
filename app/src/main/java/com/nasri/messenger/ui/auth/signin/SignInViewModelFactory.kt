package com.nasri.messenger.ui.auth.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nasri.messenger.domain.auth.signin.SignInUseCase
import timber.log.Timber


class SignInViewModelFactory(
    private val signInUseCase: SignInUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            Timber.d("Create new SignInViewModel instance")
            return SignInViewModel(signInUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
