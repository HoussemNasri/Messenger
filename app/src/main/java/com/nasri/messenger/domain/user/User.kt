package com.nasri.messenger.domain.user

data class User(
    val email: String?,
    val phone: String?,
    val uid: String,
    val username: String?,
    val lastTimestamp: Long?,
    val photoUrl: String?
)