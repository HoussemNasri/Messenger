package com.nasri.messenger.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.nasri.messenger.data.user.IAuthRepository
import com.nasri.messenger.domain.result.Event
import com.nasri.messenger.domain.result.Result
import com.nasri.messenger.domain.user.CurrentUser
import com.nasri.messenger.domain.user.GetCurrentUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val authRepository: IAuthRepository
) : ViewModel() {
    private val _navigationActions: MutableLiveData<Event<MainNavigationAction>> =
        MutableLiveData()
    val currentUser = liveData {
        val currentUser = getCurrentUserUseCase(Unit)
        if (currentUser is Result.Success) {
            emit(currentUser.data)
        } else {
            // TODO('Emit fake user object')
        }
    }

    val navigationActions: LiveData<Event<MainNavigationAction>> = _navigationActions

    // We are using GlobalScope because signOut is a transaction, it can't be stopped halfway
    fun signOut() = GlobalScope.launch(Dispatchers.IO) {
        authRepository.signOut {
            _navigationActions.postValue(Event(MainNavigationAction.NavigateToRegistrationActivityAction))
        }
    }

}

sealed class MainNavigationAction {
    object NavigateToRegistrationActivityAction : MainNavigationAction()
}