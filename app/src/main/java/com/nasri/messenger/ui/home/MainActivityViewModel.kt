package com.nasri.messenger.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nasri.messenger.data.user.IAuthRepository
import com.nasri.messenger.domain.result.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val authRepository: IAuthRepository
) : ViewModel() {
    private val _navigationActions: MutableLiveData<Event<MainNavigationAction>> =
        MutableLiveData()

    val navigationActions: LiveData<Event<MainNavigationAction>> = _navigationActions

    fun signOut() = GlobalScope.launch(Dispatchers.IO) {
        authRepository.signOut {
            _navigationActions.postValue(Event(MainNavigationAction.NavigateToRegistrationActivityAction))
        }
    }

}

sealed class MainNavigationAction {
    object NavigateToRegistrationActivityAction : MainNavigationAction()
}