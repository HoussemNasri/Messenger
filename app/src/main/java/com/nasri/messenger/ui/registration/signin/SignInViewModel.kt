package com.nasri.messenger.ui.registration.signin

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
import com.nasri.messenger.data.firebase.FirebaseConstants.Companion.FIRE_DISPLAY_NAME
import com.nasri.messenger.data.firebase.FirebaseConstants.Companion.FIRE_LAST_SIGN_IN
import com.nasri.messenger.data.firebase.FirebaseConstants.Companion.FIRE_PHOTO_URL
import com.nasri.messenger.domain.result.Result
import com.nasri.messenger.domain.user.AuthenticatedUser
import com.nasri.messenger.domain.user.FirebaseAuthenticatedUser
import com.nasri.messenger.ui.base.BaseViewModel
import timber.log.Timber


class SignInViewModel : BaseViewModel(), OnFailureListener,
    OnCanceledListener, OnSuccessListener<AuthResult> {


    private val _authenticatedUserInfo: MutableLiveData<Result<AuthenticatedUser>> =
        MutableLiveData()

    val authenticatedUserInfo: LiveData<Result<AuthenticatedUser>>
        get() = _authenticatedUserInfo

    /** Sign In using email and password */
    fun performEmailSignIn(email: String, password: String) {
        showProgress()
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnFailureListener(this)
            .addOnSuccessListener(this)
    }

    /** Sign In using credentials */
    fun performCredentialSignIn(credential: AuthCredential) {
        showProgress()
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnFailureListener(this)
            .addOnSuccessListener(this)
    }


    override fun onFailure(it: Exception) {
        hideProgress()
        _authenticatedUserInfo.postValue(Result.Error(it))
    }

    override fun onCanceled() {
        hideProgress()
    }

    override fun onSuccess(it: AuthResult?) {
        val authenticatedUser = FirebaseAuthenticatedUser(it?.user!!)
        createUserInFirestoreIfNotExist(authenticatedUser)
            .addOnSuccessListener {
                Timber.d("Last Signed In : %d", authenticatedUser.lastTimestamp)
                _authenticatedUserInfo.postValue(Result.Success(authenticatedUser))
            }.addOnFailureListener {
                onFailure(it)
            }.addOnCompleteListener {
                hideProgress()
            }
    }

    private fun createUserInFirestoreIfNotExist(userInfo: AuthenticatedUser): Task<Void> {
        val db = FirebaseFirestore.getInstance()
        val basicUserInfo = hashMapOf(
            FIRE_DISPLAY_NAME to (userInfo.displayName ?: ""),
            FIRE_PHOTO_URL to userInfo.photoUrl.toString(),
            FIRE_LAST_SIGN_IN to userInfo.lastTimestamp
        )
        val map = emptyMap<Unit, Unit>()
        db.collection(FIRE_COLL_USERS).document(userInfo.uuid!!)
            .collection(FIRE_COLL_CONTACTS).add(map)

        return db.collection(FIRE_COLL_USERS).document(userInfo.uuid!!)
            .set(basicUserInfo, SetOptions.mergeFields(FIRE_LAST_SIGN_IN))
    }


}