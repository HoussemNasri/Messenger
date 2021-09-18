package com.nasri.messenger.ui.auth.signup

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nasri.messenger.domain.auth.signup.SignUpUseCase
import com.nasri.messenger.domain.auth.signup.SignUpUseCaseParams
import com.nasri.messenger.domain.result.Result
import com.nasri.messenger.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase
) : BaseViewModel() {
    private val _userSignedUpEvent: MutableLiveData<Result<Unit>> = MutableLiveData()

    val userSignedUpEvent: LiveData<Result<Unit>> = _userSignedUpEvent

    @SuppressLint("NullSafeMutableLiveData")
    fun signUpWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _userSignedUpEvent.postValue(Result.Loading)
            _userSignedUpEvent.postValue(signUpUseCase(SignUpUseCaseParams(email, password)))
        }
    }
}