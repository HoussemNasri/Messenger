package com.nasri.messenger.data.user

import com.google.firebase.auth.AuthCredential
import com.nasri.messenger.domain.user.CurrentUser

interface IAuthRepository {

    suspend fun setCurrentUser(currentUser: CurrentUser)

    suspend fun getCurrentUser(): CurrentUser?

    fun removeCurrentUser()

    suspend fun signInWithCredential(credential: AuthCredential)

    suspend fun signInWithEmailAndPassword(email: String, password: String)

    suspend fun signUp(email: String, password: String, photoUrl: String? = null)
}