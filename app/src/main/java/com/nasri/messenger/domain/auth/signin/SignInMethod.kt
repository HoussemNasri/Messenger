package com.nasri.messenger.domain.auth.signin

import com.google.firebase.auth.AuthCredential

sealed class SignInMethod {
    data class Credential(val credential: AuthCredential): SignInMethod()
    data class EmailAndPassword(val email: String, val password: String): SignInMethod()
}