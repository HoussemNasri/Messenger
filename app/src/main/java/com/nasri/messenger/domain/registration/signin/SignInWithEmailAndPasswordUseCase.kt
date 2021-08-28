package com.nasri.messenger.domain.registration.signin

import com.nasri.messenger.domain.UseCase
import kotlinx.coroutines.Dispatchers

data class SignInWithEmailAndPasswordUseCaseParams(val email: String, val password: String)

class SignInWithEmailAndPasswordUseCase : UseCase<SignInWithEmailAndPasswordUseCaseParams, Unit>(
    Dispatchers.IO
) {
    override suspend fun execute(parameters: SignInWithEmailAndPasswordUseCaseParams) {
        TODO("Not yet implemented")
    }
}