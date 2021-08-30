package com.nasri.messenger.data.user

import com.nasri.messenger.domain.user.User


class UserRepositoryImpl(
    private val userService: UserService
) : UserRepository {
    override suspend fun findUserById(userId: String): User? =
        userService.getUserById(userId)?.mapToUser()


    override suspend fun findUsers(query: String?, limit: Long): List<User> {
        return userService.getUsers(query, limit).map { it.mapToUser() }
    }

    override suspend fun findUserContacts(query: String?, userId: String, limit: Long): List<User> {
        return userService.getUserContacts(query = query, userId = userId, limit = limit)
            .map { it.mapToUser() }
    }

    override suspend fun insertUser(user: User) {
        userService.createUser(
            UserData(
                user.uid,
                user.username,
                user.photoUrl,
                user.email,
                user.lastTimestamp,
                user.phone
            )
        )
    }
}