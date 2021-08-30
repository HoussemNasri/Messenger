package com.nasri.messenger.data.user

import com.nasri.messenger.domain.user.User

/**
 * [UserRepository] is responsible for fetching and organizing user data from different data sources
 * */
interface UserRepository {
    /**
     * Returns the [User] of id [userId]
     * @param userId the if of the user
     * */
    suspend fun findUserById(userId: String): User?

    /**
     * Returns all users which their names match [query]
     * @param query the query to be used to search for users
     * @param limit the maximum number of users to return
     * */
    suspend fun findUsers(query: String?, limit: Long = 10): List<User>


    /**
     * Returns a list of people in contact with [userId]
     * @param userId the user
     * @param limit the maximum number of contacts to return
     * */
    suspend fun findUserContacts(query: String?, userId: String, limit: Long): List<User>

    /**
     * Create a new entry in the remote and local database with [user] information
     * */
    suspend fun insertUser(user: User)

    /**
     * Returns True if user with [email] exists in our database, False otherwise
     * @param email the email of the user to search for
     * */
    suspend fun isUserExists(email: String): Boolean
}