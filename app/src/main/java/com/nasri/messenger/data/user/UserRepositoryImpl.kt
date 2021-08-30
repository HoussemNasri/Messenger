package com.nasri.messenger.data.user

import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.nasri.messenger.domain.user.User


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