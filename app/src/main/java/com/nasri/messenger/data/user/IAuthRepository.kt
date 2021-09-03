package com.nasri.messenger.data.user

import com.google.firebase.auth.AuthCredential
import com.nasri.messenger.domain.user.CurrentUser

interface IAuthRepository {

    suspend fun getCurrentUser(): CurrentUser?

    suspend fun signInWithCredential(credential: AuthCredential)

    suspend fun signInWithEmailAndPassword(email: String, password: String)

    suspend fun signUp(email: String, password: String, photoUrl: String? = null)

    /**
     * Signs out the current user and clears it from the disk cache.
     * @param onSignOutComplete code to execute when the sign out operation is completed
     * */
    suspend fun signOut(onSignOutComplete: () -> Unit)
}