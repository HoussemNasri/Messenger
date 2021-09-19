package com.nasri.messenger.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nasri.messenger.data.user.IAuthRepository
import com.nasri.messenger.domain.user.GetCurrentUserUseCase
import timber.log.Timber

class MainActivityViewModelFactory(
    private val authRepository: IAuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            Timber.d("Create new ${modelClass.simpleName.removeSuffix("Factory")} instance")
            return MainActivityViewModel(GetCurrentUserUseCase(authRepository), authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}