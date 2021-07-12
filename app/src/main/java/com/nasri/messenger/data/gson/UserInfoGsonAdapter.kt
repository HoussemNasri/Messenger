package com.nasri.messenger.data.gson

import android.net.Uri
import com.google.firebase.auth.UserInfo

/**
 * This class is used to expose field names to Gson
 * */
class UserInfoGsonAdapter(
     userInfo: UserInfo? = null
) : UserInfo {
    private val photoUrl: Uri? = userInfo?.photoUrl
    private val displayName: String? = userInfo?.displayName
    private val email: String? = userInfo?.email
    private val phoneNumber: String? = userInfo?.phoneNumber
    private val providerId: String? = userInfo?.providerId
    private val uid: String? = userInfo?.uid
    private val isEmailVerified: Boolean = userInfo?.isEmailVerified ?: false

    override fun getPhotoUrl(): Uri? = photoUrl

    override fun getDisplayName(): String? = displayName

    override fun getEmail(): String? = email

    override fun getPhoneNumber(): String? = phoneNumber

    override fun getProviderId(): String = providerId ?: ""

    override fun getUid(): String = uid ?: ""

    override fun isEmailVerified(): Boolean = isEmailVerified
}