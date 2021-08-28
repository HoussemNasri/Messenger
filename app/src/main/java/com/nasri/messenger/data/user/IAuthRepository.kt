package com.nasri.messenger.data.user

import com.nasri.messenger.domain.user.CurrentUser

interface IAuthRepository {

    fun setCurrentUser(currentUser: CurrentUser)

    fun getCurrentUser(): CurrentUser?

    fun removeCurrentUser()
}