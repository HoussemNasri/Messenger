package com.nasri.messenger.data.user

import com.nasri.messenger.domain.user.AuthenticatedUser

/**
 * This repository manages the data of people that [AuthenticatedUser] NOT in contact with
 * */
class PeopleRepository(
    private val userService: UserService
) {

    suspend fun getPeopleById(id: String): UserData.People? =
        userService.getUserById(id)?.mapToPeople()

    suspend fun getPeople(query: String?, limit: Long): List<UserData.People> {
        return userService.getUsers(query, limit).map { it.mapToPeople() }
    }

}