package com.nasri.messenger.data.user

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.nasri.messenger.data.PreferenceStorage
import com.nasri.messenger.domain.user.CurrentUser
import com.nasri.messenger.domain.user.FirebaseCurrentUser
import com.nasri.messenger.domain.user.User
import timber.log.Timber
import java.util.concurrent.TimeUnit

class FirebaseAuthRepository(
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository,
    private val preferenceStorage: PreferenceStorage
) : IAuthRepository {
    override suspend fun setCurrentUser(currentUser: CurrentUser) {
        preferenceStorage.setCurrentUser(currentUser)
    }

    override suspend fun getCurrentUser(): CurrentUser? = preferenceStorage.getCurrentUser()

    override fun removeCurrentUser() {
        preferenceStorage.removeCurrentUser()
    }

    override suspend fun signInWithCredential(credential: AuthCredential) {
        val signInTask = auth.signInWithCredential(credential)
        val result = await(signInTask)
        if (signInTask.isSuccessful) {
            onSignInSucceed(result)
        } else {
            throw signInTask.exception!!
        }
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String) {
        val signInTask = auth.signInWithEmailAndPassword(email, password)
        val result = await(signInTask)
        if (signInTask.isSuccessful) {
            onSignInSucceed(result)
        } else {
            throw signInTask.exception!!
        }
    }

    private suspend fun onSignInSucceed(authResult: AuthResult?) {
        if (authResult != null) {
            val signedInUser = FirebaseCurrentUser(authResult.user!!)
            userRepository.insertUser(user(signedInUser))
            setCurrentUser(signedInUser)
        } else {
            throw NullPointerException("Firebase AuthResult is null")
        }
    }

    private fun <TResult> await(task: Task<TResult>): TResult {
        return Tasks.await(task, 10, TimeUnit.SECONDS)
    }

    private fun user(currentUser: FirebaseCurrentUser) = User(
        currentUser.email,
        currentUser.phone,
        currentUser.uid!!,
        currentUser.username,
        currentUser.lastTimestamp,
        currentUser.photoUrl
    )

}