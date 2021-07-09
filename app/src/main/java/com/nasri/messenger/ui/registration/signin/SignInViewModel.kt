package com.nasri.messenger.ui.registration.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCanceledListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.nasri.messenger.domain.result.Event
import com.nasri.messenger.domain.result.Result
import com.nasri.messenger.domain.user.AuthenticatedUserInfo
import com.nasri.messenger.domain.user.FirebaseUserInfo


class SignInViewModel : ViewModel(), OnCompleteListener<AuthResult>, OnFailureListener,
    OnCanceledListener {


    private val _authenticatedUserInfo: MutableLiveData<Result<AuthenticatedUserInfo>> =
        MutableLiveData()

    val authenticatedUserInfo: LiveData<Result<AuthenticatedUserInfo>>
        get() = _authenticatedUserInfo

    val dismissProgressDialogAction: MutableLiveData<Event<Unit>> = MutableLiveData()


    /** Sign In using email and password */
    fun performEmailSignIn(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this)
            .addOnFailureListener(this)
    }

    /** Sign In using credentials */
    fun performCredentialSignIn(credential: AuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this)
            .addOnFailureListener(this)

    }


    override fun onComplete(it: Task<AuthResult>) {
        if (it.isSuccessful) {
            val firebaseUser = it.result?.user
            _authenticatedUserInfo.postValue(Result.Success(FirebaseUserInfo(firebaseUser)))
            dismissProgressDialogAction.postValue(Event(Unit))
        }
    }

    override fun onFailure(it: Exception) {
        _authenticatedUserInfo.postValue(Result.Error(it))
        dismissProgressDialogAction.postValue(Event(Unit))
    }

    override fun onCanceled() {
        dismissProgressDialogAction.postValue(Event(Unit))
    }


}