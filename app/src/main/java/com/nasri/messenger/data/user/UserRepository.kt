package com.nasri.messenger.data.user

/**
 * [UserRepository] is responsible for fetching and organizing user data from different data sources
 * */
interface UserRepository {
    /**
     * Returns the [UserEntity] of id [userId]
     * @param userId the if of the user
     * */
    suspend fun findUserById(userId: String): UserEntity?

    /**
     * Returns all users which their names match [query]
     * @param query the query to be used to search for users
     * @param limit the maximum number of users to return
     * */
    fun findUserByQuery(query: String, limit: Long): List<UserEntity>

/*    *//**
     * Returns the currently connected user information
     * *//*
    fun getCurrentUser(): AuthenticatedUser?

    *//**
     * Change the current user info
     * *//*
    fun setCurrentUser(user: AuthenticatedUser?)*/

    /**
     * Returns a list of people in contact with [userId]
     * @param userId the user
     * @param limit the maximum number of contacts to return
     * */
    fun getUserContacts(userId: String, limit: Long): List<UserEntity>
}