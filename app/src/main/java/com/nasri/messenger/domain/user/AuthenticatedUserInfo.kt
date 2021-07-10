package com.nasri.messenger.domain.user

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserInfo
import timber.log.Timber

/**
 * Basic user info.
 */
interface AuthenticatedUserInfo {

    fun getEmail(): String?

    fun getLastSignInTimestamp(): Long?

    fun getCreationTimestamp(): Long?

    fun isAnonymous(): Boolean?

    fun getPhoneNumber(): String?

    fun getUid(): String?

    fun isEmailVerified(): Boolean?

    fun getDisplayName(): String?

    fun getPhotoUrl(): Uri?

    fun getProviderId(): String?

    fun getProviderData(): List<UserInfo>
}


open class FirebaseUserInfo(
    private val firebaseUser: FirebaseUser?
) : AuthenticatedUserInfo {

    override fun getEmail(): String? = firebaseUser?.email

    override fun isAnonymous(): Boolean? = firebaseUser?.isAnonymous

    override fun getPhoneNumber(): String? = firebaseUser?.phoneNumber

    override fun getUid(): String? = firebaseUser?.uid

    override fun isEmailVerified(): Boolean? = firebaseUser?.isEmailVerified

    override fun getDisplayName(): String? = firebaseUser?.displayName

    override fun getPhotoUrl(): Uri? = firebaseUser?.photoUrl

    override fun getProviderId(): String? = firebaseUser?.providerId

    override fun getProviderData(): List<UserInfo> = firebaseUser?.providerData ?: emptyList()

    override fun getLastSignInTimestamp(): Long? = firebaseUser?.metadata?.lastSignInTimestamp

    override fun getCreationTimestamp(): Long? = firebaseUser?.metadata?.creationTimestamp
}

/** Immutable data structure to share user info from local storage. */
open class LocalUserInfo(
    private val email: String?,
    private val isAnonymous: Boolean?,
    private val phone: String?,
    private val uuid: String?,
    private val isEmailVerified: Boolean?,
    private val displayName: String?,
    private val photoUrl: String?,
    private val providerId: String?,
    private val lastTimestamp: Long?,
    private val creationTimestamp: Long?,
    private val providerData: List<UserInfo>,
) : AuthenticatedUserInfo {
    override fun getEmail(): String? = email

    override fun getLastSignInTimestamp(): Long? = lastTimestamp

    override fun getCreationTimestamp(): Long? = creationTimestamp

    override fun isAnonymous(): Boolean? = isAnonymous

    override fun getPhoneNumber(): String? = phone

    override fun getUid(): String? = uuid

    override fun isEmailVerified(): Boolean? = isEmailVerified

    override fun getDisplayName(): String? = displayName

    override fun getPhotoUrl(): Uri? = Uri.parse(photoUrl)

    override fun getProviderId(): String? = providerId

    override fun getProviderData(): List<UserInfo> = providerData

}