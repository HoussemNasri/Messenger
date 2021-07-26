package com.nasri.messenger.data.user

import com.nasri.messenger.domain.user.AuthenticatedUserInfo

/**
 * This repository manages the data of [AuthenticatedUserInfo] contacts
 * */
class ContactRepository(
    private val authenticatedUserId: String,
    private val userProviderService: UserProviderService,
) {

    suspend fun getContactById(id: String): UserData.Contact? {
        TODO("load all contacts first")
    }


    suspend fun getAllContacts(): List<UserData.Contact>? {
        TODO()
    }


}