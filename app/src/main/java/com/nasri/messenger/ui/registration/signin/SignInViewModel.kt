package com.nasri.messenger.ui.registration.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import com.nasri.messenger.domain.result.Event


class SignInViewModel : ViewModel() {



    private val _userAuthenticated : MutableLiveData<Result<Boolean>> = MutableLiveData()

    val userAuthenticated : LiveData<Result<Boolean>>
        get() = _userAuthenticated

    val dismissProgressDialogAction: MutableLiveData<Event<Unit>> = MutableLiveData()


    fun onEmailSignIn(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val firebaseUser = it.result?.user
                    _userAuthenticated.postValue(Result.success(true))
                    dismissProgressDialogAction.postValue(Event(Unit))
                }
            }.addOnFailureListener {
                _userAuthenticated.postValue(Result.failure(it))
                dismissProgressDialogAction.postValue(Event(Unit))
            }
    }

    fun onGoogleSignIn() {

    }

    fun onFacebookSignIn() {

    }

}