package com.nasri.messenger.ui.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nasri.messenger.domain.auth.signup.SignUpUseCase
import timber.log.Timber

class SignUpViewModelFactory(
    private val signUpUseCase: SignUpUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            Timber.d("Create new SignUpViewModel instance")
            return SignUpViewModel(signUpUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
