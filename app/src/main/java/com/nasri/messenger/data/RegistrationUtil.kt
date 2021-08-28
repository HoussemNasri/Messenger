package com.nasri.messenger.data

import android.app.Activity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.TimeUnit

object RegistrationUtil {

     fun signOutGoogle(activity: Activity) {
        val googleSignInClient = GoogleSignIn.getClient(
            activity,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        )
        Timber.d("Sign Out Google")
        Tasks.await(googleSignInClient.signOut(), 20, TimeUnit.SECONDS)
        Timber.d("Sign Out Google - 2")
    }

    fun signOutFacebook() {

    }

    fun signOutFirebase() {
        FirebaseAuth.getInstance().signOut()
    }

    fun signOutLocalProvider(preferenceStorage: PreferenceStorage) {
        preferenceStorage.removeCurrentUser()
    }

    suspend fun signOutAllProviders(activity: Activity) {
        withContext(Dispatchers.IO) {
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