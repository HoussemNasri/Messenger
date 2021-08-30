package com.nasri.messenger.ui.auth.signup

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCanceledListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nasri.messenger.data.firebase.FirebaseConstants
import com.nasri.messenger.domain.auth.signup.SignUpUseCase
import com.nasri.messenger.domain.auth.signup.SignUpUseCaseParams
import com.nasri.messenger.domain.result.Result
import com.nasri.messenger.domain.user.CurrentUser
import com.nasri.messenger.domain.user.FirebaseCurrentUser
import com.nasri.messenger.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.RuntimeException

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase
) : BaseViewModel() {
    private val _userSignedUpEvent: MutableLiveData<Result<Unit>> = MutableLiveData()

    val userSignedUpEvent: LiveData<Result<Unit>> = _userSignedUpEvent

    @SuppressLint("NullSafeMutableLiveData")
    fun signUpWithEmailAndPassword(email: String, password: String) {
        GlobalScope.launch(Dispatchers.Main) {
            _userSignedUpEvent.postValue(Result.Loading)
            _userSignedUpEvent.postValue(signUpUseCase(SignUpUseCaseParams(email, password)))
        }
    }
}