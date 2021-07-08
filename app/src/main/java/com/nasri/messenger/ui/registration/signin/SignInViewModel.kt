package com.nasri.messenger.ui.registration.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.nasri.messenger.domain.result.Event
import com.nasri.messenger.domain.user.AuthenticatedUserInfo
import com.nasri.messenger.domain.result.Result
import com.nasri.messenger.domain.user.FirebaseUserInfo


class SignInViewModel : ViewModel() {


    private val _authenticatedUserInfo: MutableLiveData<Result<AuthenticatedUserInfo>> =
        MutableLiveData()

    val authenticatedUserInfo: LiveData<Result<AuthenticatedUserInfo>>
        get() = _authenticatedUserInfo

    val dismissProgressDialogAction: MutableLiveData<Event<Unit>> = MutableLiveData()


    fun onEmailSignIn(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val firebaseUser = it.result?.user
                    _authenticatedUserInfo.postValue(Result.Success(FirebaseUserInfo(firebaseUser)))
                    dismissProgressDialogAction.postValue(Event(Unit))
                }
            }.addOnFailureListener {
                _authenticatedUserInfo.postValue(Result.Error(it))
                dismissProgressDialogAction.postValue(Event(Unit))
            }
    }

    fun onGoogleSignIn() {

    }

    fun onFacebookSignIn() {

    }

}