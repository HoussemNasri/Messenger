package com.nasri.messenger.domain.user

import com.nasri.messenger.data.user.IAuthRepository
import com.nasri.messenger.domain.UseCase
import kotlinx.coroutines.Dispatchers

class GetCurrentUserUseCase(
    private val authRepository: IAuthRepository
) : UseCase<Unit, CurrentUser>(Dispatchers.IO) {
    override suspend fun execute(parameters: Unit): CurrentUser =
        authRepository.getCurrentUser() ?: throw NoUserLoggedInException()
}