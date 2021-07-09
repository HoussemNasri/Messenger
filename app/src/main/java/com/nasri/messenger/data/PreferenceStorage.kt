package com.nasri.messenger.data

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import com.nasri.messenger.domain.user.AuthenticatedUserInfo
import com.nasri.messenger.domain.user.LocalUserInfo
import timber.log.Timber
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 * Storage for app and user preferences.
 */
interface PreferenceStorage {
    var isUserLoggedIn: Boolean
    fun saveAuthenticatedUser(userInfo: AuthenticatedUserInfo)
    fun removeAuthenticatedUser()
    fun getCurrentUserInfo(): AuthenticatedUserInfo?
}

/**
 * [PreferenceStorage] impl backed by [android.content.SharedPreferences].
 */

class SharedPreferenceStorage constructor(
    context: Context
) : PreferenceStorage {

    private val prefs: Lazy<SharedPreferences> = lazy { // Lazy to prevent IO access to main thread.
        context.applicationContext.getSharedPreferences(
            PREFS_NAME, Context.MODE_PRIVATE
        ).apply {
            if (!this.contains(IS_SIGNED_IN)) {
                this.edit().putBoolean(IS_SIGNED_IN, false).apply()
            }
        }
    }

    override var isUserLoggedIn by BooleanPreference(prefs, IS_SIGNED_IN, false)

    override fun saveAuthenticatedUser(userInfo: AuthenticatedUserInfo) {
        prefs.value.edit().apply {
            putString(AUTH_USER_EMAIL, userInfo.getEmail())
            putBoolean(AUTH_IS_ANONYMOUS, userInfo.isAnonymous() ?: false)
            putString(AUTH_PHONE_NUMBER, userInfo.getPhoneNumber())
            putString(AUTH_UUID, userInfo.getUid())
            putBoolean(AUTH_IS_EMAIL_VERIFIED, userInfo.isEmailVerified() ?: false)
            putString(AUTH_DISPLAY_NAME, userInfo.getDisplayName())
            putString(AUTH_PHOTO_URL, userInfo.getPhotoUrl().toString())
            putString(AUTH_PROVIDER_ID, userInfo.getProviderId())
            putLong(AUTH_LAST_SIGNIN, userInfo.getLastSignInTimestamp() ?: -1)
            putLong(AUTH_ACCOUNT_CREATION, userInfo.getCreationTimestamp() ?: -1)
            putBoolean(IS_SIGNED_IN, true)
        }.apply()

    }

    override fun removeAuthenticatedUser() {
        prefs.value.edit().apply {
            remove(AUTH_USER_EMAIL)
            remove(AUTH_IS_ANONYMOUS)
            remove(AUTH_PHONE_NUMBER)
            remove(AUTH_UUID)
            remove(AUTH_IS_EMAIL_VERIFIED)
            remove(AUTH_DISPLAY_NAME)
            remove(AUTH_PHOTO_URL)
            remove(AUTH_PROVIDER_ID)
            remove(AUTH_LAST_SIGNIN)
            remove(AUTH_ACCOUNT_CREATION)
            putBoolean(IS_SIGNED_IN, false)
        }.apply()
    }

    override fun getCurrentUserInfo(): AuthenticatedUserInfo? {
        if (!isUserLoggedIn) {
            return null
        }
        val email = prefs.value.getString(AUTH_USER_EMAIL, "null")
        val isAnonymous = prefs.value.getBoolean(AUTH_IS_ANONYMOUS, false)
        val phoneNumber = prefs.value.getString(AUTH_PHONE_NUMBER, "-1")
        val userID = prefs.value.getString(AUTH_UUID, "-1")
        val isEmailVerified = prefs.value.getBoolean(AUTH_IS_EMAIL_VERIFIED, false)
        val displayName = prefs.value.getString(AUTH_DISPLAY_NAME, "Anonymous")
        val photoUrl = prefs.value.getString(AUTH_PHOTO_URL, "")
        val providerID = prefs.value.getString(AUTH_PROVIDER_ID, "")
        val lastTimestamp = prefs.value.getLong(AUTH_LAST_SIGNIN, -1L)
        val creationTimestamp = prefs.value.getLong(AUTH_ACCOUNT_CREATION, -1L)

        return LocalUserInfo(
            email, isAnonymous, phoneNumber, userID,
            isEmailVerified, displayName, photoUrl, providerID, lastTimestamp, creationTimestamp
        )

    }

    companion object {
        const val PREFS_NAME = "messenger"
        const val IS_SIGNED_IN = "is_signed_in"
        const val AUTH_USER_EMAIL = "user_email"
        const val AUTH_IS_ANONYMOUS = "user_is_anonymous"
        const val AUTH_PHONE_NUMBER = "user_phone_number"
        const val AUTH_UUID = "user_id"
        const val AUTH_IS_EMAIL_VERIFIED = "user_is_email_verified"
        const val AUTH_DISPLAY_NAME = "user_display_name"
        const val AUTH_PHOTO_URL = "user_photo_url"
        const val AUTH_PROVIDER_ID = "user_provider_id"
        const val AUTH_LAST_SIGNIN = "user_last_sign_in_timestamp"
        const val AUTH_ACCOUNT_CREATION = "user_account_creation_timestamp"
    }

    class BooleanPreference(
        private val preferences: Lazy<SharedPreferences>,
        private val name: String,
        private val defaultValue: Boolean
    ) : ReadWriteProperty<Any, Boolean> {

        @WorkerThread
        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
            return preferences.value.getBoolean(name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
            preferences.value.edit { putBoolean(name, value) }
        }
    }
}