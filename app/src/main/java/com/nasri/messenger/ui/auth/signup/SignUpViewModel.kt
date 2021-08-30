package com.nasri.messenger.ui.auth.signup

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
import com.nasri.messenger.domain.result.Result
import com.nasri.messenger.domain.user.CurrentUser
import com.nasri.messenger.domain.user.FirebaseCurrentUser
import com.nasri.messenger.ui.base.BaseViewModel
import java.lang.RuntimeException

class SignUpViewModel : BaseViewModel(), OnSuccessListener<AuthResult>, OnFailureListener,
    OnCanceledListener {
    private val _userSignedUp: MutableLiveData<Result<Unit>> = MutableLiveData()

    val userSignedUp: LiveData<Result<Unit>> = _userSignedUp

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
        addUserToFirestore(FirebaseCurrentUser(it?.user!!)).addOnCompleteListener {
            _userSignedUp.postValue(Result.Success(Unit))
        }.addOnFailureListener {
            _userSignedUp.postValue(Result.Error(it))
        }

    }

    override fun onFailure(it: Exception) {
        hideProgress()
        _userSignedUp.postValue(Result.Error(it))
    }

    override fun onCanceled() {
        hideProgress()
        _userSignedUp.postValue(Result.Error(RuntimeException("Sign up operation canceled")))
    }

    private fun addUserToFirestore(userInfo: CurrentUser): Task<Void> {
        val db = FirebaseFirestore.getInstance()
        val basicUserInfo = hashMapOf(
            FirebaseConstants.FIRE_USERNAME to (userInfo.username ?: "Jhon Doe"),
            FirebaseConstants.FIRE_PHOTO_URL to generateRandomAvatar(userInfo.uid),
            FirebaseConstants.FIRE_LAST_SIGN_IN to userInfo.lastTimestamp,
            FirebaseConstants.FIRE_EMAIL to userInfo.email,
            FirebaseConstants.FIRE_ACCOUNT_CREATION to userInfo.creationTimestamp,
            FirebaseConstants.FIRE_PHONE to userInfo.phone
        )
        val map = emptyMap<Unit, Unit>()
        db.collection(FirebaseConstants.FIRE_COLL_USERS).document(userInfo.uid!!)
            .collection(FirebaseConstants.FIRE_COLL_CONTACTS).add(map)

        return db.collection(FirebaseConstants.FIRE_COLL_USERS).document(userInfo.uid!!)
            .set(basicUserInfo)
    }

    private fun generateRandomAvatar(seed: String?): String {
        return "https://avatars.dicebear.com/api/bottts/${seed ?: 123}.svg"
    }
}