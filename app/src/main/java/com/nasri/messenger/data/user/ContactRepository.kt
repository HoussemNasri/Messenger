package com.nasri.messenger.data.user

import com.nasri.messenger.domain.user.AuthenticatedUser

/**
 * This repository manages the data of [AuthenticatedUser] contacts
 * */
class ContactRepository(
    private val authenticatedUserId: String,
    private val userService: UserService,
) {

    suspend fun getContactById(id: String): UserData.Contact? {
        return userService.getContactById(authenticatedUserId, id)?.mapToContact()
    }

    suspend fun getContacts(query: String?, limit: Long): List<UserData.Contact> {
        return userService.getUserContacts(authenticatedUserId, query, limit).map {
            it.mapToContact()
        }
    }

    suspend fun getContacts(limit: Long): List<UserData.Contact> = getContacts(null, limit)


}