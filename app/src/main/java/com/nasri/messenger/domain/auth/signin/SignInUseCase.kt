package com.nasri.messenger.domain.auth.signin

import com.nasri.messenger.data.user.IAuthRepository
import com.nasri.messenger.domain.UseCase
import kotlinx.coroutines.Dispatchers

class SignInUseCase(
    private val authRepository: IAuthRepository
) : UseCase<SignInMethod, Unit>(Dispatchers.IO) {

    override suspend fun execute(parameters: SignInMethod) {
        when (parameters) {
            is SignInMethod.Credential -> {
                authRepository.signInWithCredential(parameters.credential)
            }
            is SignInMethod.EmailAndPassword -> {
                authRepository.signInWithEmailAndPassword(parameters.email, parameters.password)
            }
        }
    }
}