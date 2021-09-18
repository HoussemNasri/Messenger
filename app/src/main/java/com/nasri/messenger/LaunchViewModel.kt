package com.nasri.messenger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.nasri.messenger.domain.prefs.UserLoggedInUseCase
import com.nasri.messenger.domain.result.data

class LaunchViewModel constructor(
    private val userLoggedInUseCase: UserLoggedInUseCase
) : ViewModel() {
    val launchDestination = liveData {
        val result = userLoggedInUseCase(Unit)
        if (result.data == true) {
            emit(LaunchDestination.MAIN_ACTIVITY)
        } else {
            emit(LaunchDestination.REGISTRATION)
        }
    }
}

enum class LaunchDestination {
    REGISTRATION,
    MAIN_ACTIVITY,
}