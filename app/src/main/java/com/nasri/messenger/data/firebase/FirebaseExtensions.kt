package com.nasri.messenger.data.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.nasri.messenger.data.user.UserData

fun DocumentSnapshot.toUserData(): UserData =
    UserData(
        id,
        getString(FirebaseConstants.FIRE_DISPLAY_NAME) ?: "",
        getString(FirebaseConstants.FIRE_PHOTO_URL) ?: "",
        getString(FirebaseConstants.FIRE_EMAIL),
        getLong(FirebaseConstants.FIRE_LAST_SIGN_IN)
    )