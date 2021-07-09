package com.nasri.messenger.data

import android.app.Activity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

object RegistrationUtil {

    fun signOutGoogle(activity: Activity) {
        val googleSignInClient = GoogleSignIn.getClient(
            activity,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        )
        googleSignInClient.signOut()
    }

    fun signOutFacebook() {

    }

    fun signOutFirebase() {
        FirebaseAuth.getInstance().signOut()
    }

    fun signOutLocalProvider(preferenceStorage: PreferenceStorage) {
        preferenceStorage.removeAuthenticatedUser()
    }

    fun signOutAllProviders(activity: Activity) {
        val preferenceStorage = SharedPreferenceStorage(activity)
        if (preferenceStorage.isUserLoggedIn) {
            signOutLocalProvider(preferenceStorage)
        }
        val providerData = FirebaseAuth.getInstance().currentUser?.providerData ?: emptyList()
        providerData.forEach {
            val providerId = it.providerId
            when (Provider.parse(providerId)) {
                Provider.GOOGLE -> signOutGoogle(activity)
                Provider.FIREBASE -> signOutFirebase()
                Provider.FACEBOOK -> signOutFacebook()
                Provider.UNKNOWN -> Timber.d("signOutAllProviders : UnknownProvider")
            }
        }

    }

    enum class Provider(val id: String) {
        GOOGLE("google.com"), FACEBOOK("facebook.com"), FIREBASE("firebase"), UNKNOWN("");

        companion object {
            fun parse(id: String): Provider {
                return when (id) {
                    "google.com" -> GOOGLE
                    "facebook.com" -> FACEBOOK
                    "firebase" -> FIREBASE
                    else -> UNKNOWN
                }
            }
        }

    }
}