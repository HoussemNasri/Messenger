package com.nasri.messenger.ui.auth.signin

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.nasri.messenger.domain.auth.signin.SignInMethod
import com.nasri.messenger.domain.auth.signin.SignInUseCase
import com.nasri.messenger.domain.result.Result
import com.nasri.messenger.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SignInViewModel(
    private val signInUseCase: SignInUseCase
) : BaseViewModel() {

    private val _userSignedInEvent: MutableLiveData<Result<Unit>> =
        MutableLiveData()

    val userSignedInEvent: LiveData<Result<Unit>> = _userSignedInEvent

    /** Sign In using email and password */
    @SuppressLint("NullSafeMutableLiveData")
    fun performEmailSignIn(email: String, password: String) {
        // TODO('Use a custom coroutine scope')
        viewModelScope.launch(Dispatchers.Main) {
            _userSignedInEvent.postValue(Result.Loading)
            _userSignedInEvent.postValue(
                signInUseCase(
                    SignInMethod.EmailAndPassword(
                        email,
                        password
                    )
                )
            )
        }
    }

    /** Sign In using credentials */
    @SuppressLint("NullSafeMutableLiveData")
    fun performCredentialSignIn(credential: AuthCredential) {
        viewModelScope.launch(Dispatchers.Main) {
            _userSignedInEvent.postValue(Result.Loading)
            _userSignedInEvent.postValue(signInUseCase(SignInMethod.Credential(credential)))
        }
    }

}