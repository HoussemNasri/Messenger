package com.nasri.messenger.domain.registration.signin

import com.google.android.gms.auth.api.credentials.Credential
import com.nasri.messenger.domain.UseCase
import kotlinx.coroutines.Dispatchers

class SignInWithCredentialUseCase : UseCase<Credential, Unit>(
    Dispatchers.IO
) {
    override suspend fun execute(parameters: Credential) {
        TODO("Not yet implemented")
    }

}