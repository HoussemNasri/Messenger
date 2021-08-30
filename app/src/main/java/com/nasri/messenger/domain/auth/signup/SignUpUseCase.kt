package com.nasri.messenger.domain.auth.signup

import com.nasri.messenger.data.user.IAuthRepository
import com.nasri.messenger.domain.UseCase
import kotlinx.coroutines.Dispatchers

data class SignUpUseCaseParams(
    val email: String, val password: String, val photoUrl: String? = null
)

class SignUpUseCase(
    private val authRepository: IAuthRepository
) : UseCase<SignUpUseCaseParams, Unit>(Dispatchers.IO) {
    override suspend fun execute(parameters: SignUpUseCaseParams) {
        authRepository.signUp(parameters.email, parameters.password)
    }
}