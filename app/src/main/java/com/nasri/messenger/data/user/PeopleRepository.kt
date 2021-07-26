package com.nasri.messenger.data.user

import com.nasri.messenger.domain.user.AuthenticatedUserInfo

/**
 * This repository manages the data of people that [AuthenticatedUserInfo] NOT in contact with
 * */
class PeopleRepository(
    private val userProviderService: UserProviderService
) {

    suspend fun getPeopleById(id: String): UserData.People? =
        userProviderService.getUserById(id)?.mapToPeople()

    suspend fun getPeopleList(page: Int, itemsPerPage: Int = 30): List<UserData.People>? {
        TODO()
    }

}