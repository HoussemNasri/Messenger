package com.nasri.messenger.data.user

import com.nasri.messenger.data.PreferenceStorage
import com.nasri.messenger.domain.user.CurrentUser

class FirebaseAuthRepository(
    private val preferenceStorage: PreferenceStorage
) : IAuthRepository {
    override fun setCurrentUser(currentUser: CurrentUser) {
        preferenceStorage.setCurrentUser(currentUser)
    }

    override fun getCurrentUser(): CurrentUser? = preferenceStorage.getCurrentUser()

    override fun removeCurrentUser() {
        preferenceStorage.removeCurrentUser()
    }

}