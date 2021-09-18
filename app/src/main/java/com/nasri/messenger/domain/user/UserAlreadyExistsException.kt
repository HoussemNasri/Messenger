package com.nasri.messenger.domain.user

class UserAlreadyExistsException(private val msg: String? = null) : RuntimeException(msg) {
}