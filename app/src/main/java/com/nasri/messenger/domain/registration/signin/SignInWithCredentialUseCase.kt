package com.nasri.messenger.domain.registration.signin

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.nasri.messenger.data.user.IAuthRepository
import com.nasri.messenger.domain.UseCase
import com.nasri.messenger.domain.user.FirebaseCurrentUser
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.TimeUnit

class SignInWithCredentialUseCase(
    private val auth: FirebaseAuth,
    private val authRepository: IAuthRepository
) : UseCase<AuthCredential, Unit>(
    Dispatchers.IO
), OnFailureListener, OnSuccessListener<AuthResult> {
    override suspend fun execute(parameters: AuthCredential) {
        Tasks.await(
            auth.signInWithCredential(parameters)
                .addOnFailureListener(this)
                .addOnSuccessListener(this), 10, TimeUnit.SECONDS
        )
    }

    override fun onFailure(e: Exception) = throw e

    override fun onSuccess(result: AuthResult?) {
        if (result != null) {
            //authRepository.setCurrentUser(FirebaseCurrentUser(result.user!!))
        } else {
            throw NullPointerException("Firebase AuthResult is Null")
        }
    }

}