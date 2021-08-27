package com.nasri.messenger.domain.user

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserInfo


abstract class AuthenticatedUser {
    abstract var email: String?
    abstract var isAnonymous: Boolean?
    abstract var phone: String?
    abstract var uuid: String?
    abstract var isEmailVerified: Boolean?
    abstract var displayName: String?
    abstract var photoUrl: String?
    abstract var providerId: String?
    abstract var lastTimestamp: Long?
    abstract var creationTimestamp: Long?
    abstract var providerData: List<UserInfo>
}

/**
 * Delegates [AuthenticatedUser] calls to a [FirebaseUser] to be used in production.
 * */
open class FirebaseAuthenticatedUser(
    private val firebaseUser: FirebaseUser
) : AuthenticatedUser() {
    override var email: String? = firebaseUser.email
    override var isAnonymous: Boolean? = firebaseUser.isAnonymous
    override var phone: String? = firebaseUser.phoneNumber
    override var uuid: String? = firebaseUser.uid
    override var isEmailVerified: Boolean? = firebaseUser.isEmailVerified
    override var displayName: String? = firebaseUser.displayName
    override var photoUrl: String? = firebaseUser.photoUrl.toString()
    override var providerId: String? = firebaseUser.providerId
    override var lastTimestamp: Long? = firebaseUser.metadata?.lastSignInTimestamp
    override var creationTimestamp: Long? = firebaseUser.metadata?.creationTimestamp
    override var providerData: List<UserInfo> = firebaseUser.providerData
}

/**
 * Simple implementation of [AuthenticatedUser] to be used primarily in SharedPreferences
 * */
data class SimpleAuthenticatedUser(
    override var email: String?,
    override var isAnonymous: Boolean?,
    override var phone: String?,
    override var uuid: String?,
    override var isEmailVerified: Boolean?,
    override var displayName: String?,
    override var photoUrl: String?,
    override var providerId: String?,
    override var lastTimestamp: Long?,
    override var creationTimestamp: Long?,
    override var providerData: List<UserInfo>
) : AuthenticatedUser()