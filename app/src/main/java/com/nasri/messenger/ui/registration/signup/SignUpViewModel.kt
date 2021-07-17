package com.nasri.messenger.ui.registration.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCanceledListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.nasri.messenger.domain.result.Result
import com.nasri.messenger.ui.base.BaseViewModel
import java.lang.RuntimeException

class SignUpViewModel : BaseViewModel(), OnSuccessListener<AuthResult>, OnFailureListener,
    OnCanceledListener {
    private val _userSignedUp: MutableLiveData<Result<Unit>> = MutableLiveData()

    val userSignedUp: LiveData<Result<Unit>>
        get() = _userSignedUp

    fun signUpWithEmailAndPassword(email: String, password: String) {
        showProgress()
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener(this)
            .addOnFailureListener(this)
            .addOnCanceledListener(this)

    }

    override fun onSuccess(it: AuthResult?) {
        hideProgress()
        _userSignedUp.postValue(Result.Success(Unit))
    }

    override fun onFailure(it: Exception) {
        hideProgress()
        _userSignedUp.postValue(Result.Error(it))
    }

    override fun onCanceled() {
        hideProgress()
        _userSignedUp.postValue(Result.Error(RuntimeException("Sign up operation canceled")))
    }
}