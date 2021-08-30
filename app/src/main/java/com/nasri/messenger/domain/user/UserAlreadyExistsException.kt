package com.nasri.messenger.domain.user

import java.lang.RuntimeException

class UserAlreadyExistsException(private val msg: String? = null) : RuntimeException(msg) {
}