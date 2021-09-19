package com.nasri.messenger.data.user

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.nasri.messenger.data.PreferenceStorage
import com.nasri.messenger.data.user.util.UsernameGenerator
import com.nasri.messenger.domain.user.CurrentUser
import com.nasri.messenger.domain.user.FirebaseCurrentUser
import com.nasri.messenger.domain.user.User
import com.nasri.messenger.domain.user.UserAlreadyExistsException
import timber.log.Timber
import java.util.concurrent.TimeUnit

class FirebaseAuthRepository(
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository,
    private val preferenceStorage: PreferenceStorage
) : IAuthRepository {
    private fun setCurrentUser(currentUser: CurrentUser) {
        preferenceStorage.setCurrentUser(currentUser)
    }

    override suspend fun getCurrentUser(): CurrentUser? = preferenceStorage.getCurrentUser()

    private fun removeCurrentUser() {
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

    private suspend fun onSignInSucceed(result: AuthResult?) {
        if (result == null) {
            throw NullPointerException("Firebase AuthResult is null")
        }

        val user: FirebaseUser = result.user!!
        if (!userRepository.isUserExists(user.email!!)) {
            val username = doGenerateUsername()
            val photoUrl = doGenerateRandomAvatarUrlForUser(user.uid)
            updateUserProfile(user, username, photoUrl)
            saveUserDataToDatabase(user, username, photoUrl)
        } else {
            //TODO('Update User Information like: lastSingedIn...')
        }
        setCurrentUser(FirebaseCurrentUser(auth.currentUser!!))
    }

    private fun <TResult> await(task: Task<TResult>): TResult {
        return Tasks.await(task, 10, TimeUnit.SECONDS)
    }

    override suspend fun signUp(email: String, password: String, photoUrl: String?) {
        if (userRepository.isUserExists(email)) {
            throw UserAlreadyExistsException("User already exists")
        } else {
            val signUpTask = auth.createUserWithEmailAndPassword(email, password)
            val result = await(signUpTask)
            if (signUpTask.isSuccessful) {
                val user: FirebaseUser = result.user!!
                val username = doGenerateUsername()
                val photoUrl2 = photoUrl ?: doGenerateRandomAvatarUrlForUser(user.uid)
                updateUserProfile(user, username, photoUrl2)
                saveUserDataToDatabase(
                    user,
                    username,
                    photoUrl2
                )
            } else {
                throw signUpTask.exception!!
            }
        }
    }

    private fun updateUserProfile(user: FirebaseUser, username: String, photoUrl: String) {
        val profileUpdates = userProfileChangeRequest {
            displayName = username
            photoUri = Uri.parse(photoUrl)
        }
        await(user.updateProfile(profileUpdates))
    }

    private suspend fun doGenerateUsername(): String = UsernameGenerator.generate()

    /**
     * [userId] this id will act like seed for our random function
     * */
    private fun doGenerateRandomAvatarUrlForUser(userId: String): String {
        return "https://avatars.dicebear.com/api/bottts/$userId.svg"
    }

    private suspend fun saveUserDataToDatabase(
        firebaseUser: FirebaseUser,
        username: String,
        photoUrl: String
    ) {
        userRepository.insertUser(mapFirebaseUserToUser(firebaseUser, username, photoUrl))
    }

    override suspend fun signOut(onSignOutComplete: () -> Unit) {
        Firebase.auth.signOut()
        removeCurrentUser()
        Firebase.auth.addAuthStateListener {
            onSignOutComplete()
        }
    }

    private fun mapFirebaseUserToUser(
        firebaseUser: FirebaseUser,
        username: String,
        photoUrl: String
    ): User {
        return User(
            firebaseUser.email,
            firebaseUser.phoneNumber,
            firebaseUser.uid,
            username,
            firebaseUser.metadata?.lastSignInTimestamp,
            photoUrl
        )
    }

}