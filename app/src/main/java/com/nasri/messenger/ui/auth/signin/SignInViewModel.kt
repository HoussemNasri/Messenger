package com.nasri.messenger.ui.auth.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCanceledListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.nasri.messenger.data.firebase.FirebaseConstants.Companion.FIRE_COLL_CONTACTS
import com.nasri.messenger.data.firebase.FirebaseConstants.Companion.FIRE_COLL_USERS
import com.nasri.messenger.data.firebase.FirebaseConstants.Companion.FIRE_USERNAME
import com.nasri.messenger.data.firebase.FirebaseConstants.Companion.FIRE_LAST_SIGN_IN
import com.nasri.messenger.data.firebase.FirebaseConstants.Companion.FIRE_PHOTO_URL
import com.nasri.messenger.domain.registration.signin.SignInMethod
import com.nasri.messenger.domain.registration.signin.SignInUseCase
import com.nasri.messenger.domain.result.Result
import com.nasri.messenger.domain.user.CurrentUser
import com.nasri.messenger.domain.user.FirebaseCurrentUser
import com.nasri.messenger.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber


class SignInViewModel(
    private val signInUseCase: SignInUseCase
) : BaseViewModel() {


    private val _userSignedInEvent: MutableLiveData<Result<Unit>> =
        MutableLiveData()

    val userSignedInEvent: LiveData<Result<Unit>> = _userSignedInEvent

    /** Sign In using email and password */
    fun performEmailSignIn(email: String, password: String) {
        // TODO('Use a custom coroutine scope')
        GlobalScope.launch(Dispatchers.Main) {
            _userSignedInEvent.postValue(Result.Loading)
            _userSignedInEvent.postValue(
                signInUseCase(
                    SignInMethod.EmailAndPassword(
                        email,
                        password
                    )
                )!!
            )
        }
    }

    /** Sign In using credentials */
    fun performCredentialSignIn(credential: AuthCredential) {
        GlobalScope.launch(Dispatchers.Main) {
            _userSignedInEvent.postValue(Result.Loading)
            _userSignedInEvent.postValue(
                signInUseCase(SignInMethod.Credential(credential))!!
            )
        }
    }


}