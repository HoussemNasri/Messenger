package com.nasri.messenger.ui.registration.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCanceledListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.nasri.messenger.data.firebase.FirebaseConstants
import com.nasri.messenger.domain.result.Result
import com.nasri.messenger.domain.user.AuthenticatedUser
import com.nasri.messenger.domain.user.FirebaseAuthenticatedUser
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
        val userInfo = FirebaseAuthenticatedUser(it?.user!!)
        createUserInFirestoreIfNotExist(userInfo)
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

    private fun createUserInFirestoreIfNotExist(userInfo: AuthenticatedUser): Task<Void> {
        val db = FirebaseFirestore.getInstance()
        val basicUserInfo = hashMapOf(
            FirebaseConstants.FIRE_DISPLAY_NAME to (userInfo.displayName ?: ""),
            FirebaseConstants.FIRE_PHOTO_URL to userInfo.photoUrl.toString(),
            FirebaseConstants.FIRE_LAST_SIGN_IN to userInfo.lastTimestamp
        )
        val map = emptyMap<Unit, Unit>()
        db.collection(FirebaseConstants.FIRE_COLL_USERS).document(userInfo.uuid!!)
            .collection(FirebaseConstants.FIRE_COLL_CONTACTS).add(map)

        return db.collection(FirebaseConstants.FIRE_COLL_USERS).document(userInfo.uuid!!)
            .set(basicUserInfo, SetOptions.mergeFields(FirebaseConstants.FIRE_LAST_SIGN_IN))
    }
}