package com.nasri.messenger.data.user


class UserRepositoryImpl(
    private val userService: UserService
) : UserRepository {
    override suspend fun findUserById(userId: String): UserEntity? {
        return userService.getUserById(userId)?.mapToUserEntity()
    }

    override fun findUserByQuery(query: String, limit: Long): List<UserEntity> {
        return userService.getUsers(query, limit).map { it.mapToUserEntity() }
    }

    override fun getUserContacts(userId: String, limit: Long): List<UserEntity> {
        TODO("Not yet implemented")
    }
}