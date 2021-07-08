package com.nasri.messenger.domain.prefs

import com.nasri.messenger.data.PreferenceStorage
import com.nasri.messenger.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class UserLoggedInUseCase constructor(
    private val preferenceStorage: PreferenceStorage,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UseCase<Unit, Boolean>(dispatcher) {

    override suspend fun execute(parameters: Unit): Boolean {
        return preferenceStorage.isUserLoggedIn
    }

}