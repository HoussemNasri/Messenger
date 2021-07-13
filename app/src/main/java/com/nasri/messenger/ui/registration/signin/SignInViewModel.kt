package com.nasri.messenger.ui.registration.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
import com.nasri.messenger.data.firebase.FirebaseConstants.Companion.FIRE_DISPLAY_NAME
import com.nasri.messenger.data.firebase.FirebaseConstants.Companion.FIRE_LAST_SIGN_IN
import com.nasri.messenger.data.firebase.FirebaseConstants.Companion.FIRE_PHOTO_URL
import com.nasri.messenger.domain.result.Result
import com.nasri.messenger.domain.user.AuthenticatedUserInfo
import com.nasri.messenger.domain.user.FirebaseUserInfo
import timber.log.Timber


class SignInViewModel : ViewModel(), OnFailureListener,
    OnCanceledListener, OnSuccessListener<AuthResult> {


    private val _authenticatedUserInfo: MutableLiveData<Result<AuthenticatedUserInfo>> =
        MutableLiveData()

    val authenticatedUserInfo: LiveData<Result<AuthenticatedUserInfo>>
        get() = _authenticatedUserInfo

    val showProgress = MutableLiveData(false)


    /** Sign In using email and password */
    fun performEmailSignIn(email: String, password: String) {
        showProgress.postValue(true)
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnFailureListener(this)
            .addOnSuccessListener(this)
    }

    /** Sign In using credentials */
    fun performCredentialSignIn(credential: AuthCredential) {
        showProgress.postValue(true)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnFailureListener(this)
            .addOnSuccessListener(this)
    }


    override fun onFailure(it: Exception) {
        showProgress.postValue(false)
        _authenticatedUserInfo.postValue(Result.Error(it))
    }

    override fun onCanceled() {
        showProgress.postValue(false)
    }

    override fun onSuccess(it: AuthResult?) {
        val authenticatedUser = FirebaseUserInfo(it?.user)
        createUserInFirestoreIfNotExist(authenticatedUser)
            .addOnSuccessListener {
                Timber.d("Last Signed In : %d", authenticatedUser.getLastSignInTimestamp())
                _authenticatedUserInfo.postValue(Result.Success(authenticatedUser))
            }.addOnFailureListener {
                onFailure(it)
            }.addOnCompleteListener {
                showProgress.postValue(false)
            }
    }

    private fun createUserInFirestoreIfNotExist(userInfo: AuthenticatedUserInfo): Task<Void> {
        val db = FirebaseFirestore.getInstance()
        val basicUserInfo = hashMapOf(
            FIRE_DISPLAY_NAME to (userInfo.getDisplayName() ?: ""),
            FIRE_PHOTO_URL to userInfo.getPhotoUrl().toString(),
            FIRE_LAST_SIGN_IN to userInfo.getLastSignInTimestamp()
        )
        val map = emptyMap<Unit, Unit>()
        db.collection(FIRE_COLL_USERS).document(userInfo.getUid()!!)
            .collection(FIRE_COLL_CONTACTS).add(map)

        return db.collection(FIRE_COLL_USERS).document(userInfo.getUid()!!)
            .set(basicUserInfo, SetOptions.mergeFields(FIRE_LAST_SIGN_IN))
    }


}