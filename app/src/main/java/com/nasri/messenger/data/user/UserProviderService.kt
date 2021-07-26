package com.nasri.messenger.data.user

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.nasri.messenger.data.firebase.FirebaseConstants

interface UserProviderService {
    suspend fun getUserById(id: String): UserData?
    suspend fun getUsersByNamePrefix(prefix: String): List<UserData>
    suspend fun getUsers(page: Int, itemsPerPage: Int): List<UserData>
    suspend fun getUserContacts(userId: String): List<UserData>
}

class FirebaseUserProviderService(
    private val db: FirebaseFirestore
) : UserProviderService {
    override suspend fun getUserById(id: String): UserData? {
        val task = db.collection(FirebaseConstants.FIRE_COLL_USERS)
            .document(id).get()
        val data = Tasks.await(task)

        return if (task.isSuccessful) {
            UserData(
                data.id,
                data.getString(FirebaseConstants.FIRE_DISPLAY_NAME) ?: "",
                data.getString(FirebaseConstants.FIRE_PHOTO_URL) ?: "",
                data.getString(FirebaseConstants.FIRE_EMAIL),
                data.getLong(FirebaseConstants.FIRE_LAST_SIGN_IN)
            )
        } else {
            null
        }
    }

    override suspend fun getUsersByNamePrefix(prefix: String): List<UserData> {
        TODO("Not yet implemented")
    }

    override suspend fun getUsers(page: Int, itemsPerPage: Int): List<UserData> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserContacts(userId: String): List<UserData> {
        TODO("Not yet implemented")
    }
}

open class UserData internal constructor(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val email: String?,
    val lastSignedIn: Long?
) {
    data class Contact(
        val cId: String,
        val cName: String,
        val cAvatarUrl: String,
        val cEmail: String?,
        val cLastSignedIn: Long
    ) : UserData(cId, cName, cAvatarUrl, cEmail, cLastSignedIn)

    data class People(
        val pId: String,
        val pName: String,
        val pAvatarUrl: String,
    ) : UserData(pId, pName, pAvatarUrl, null, null)
}

fun UserData.mapToContact(): UserData.Contact =
    UserData.Contact(id, name, avatarUrl, email, lastSignedIn ?: -1)

fun UserData.mapToPeople(): UserData.People = UserData.People(id, name, avatarUrl)