package com.nasri.messenger.data.user

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.nasri.messenger.data.db.RandomUsers
import com.nasri.messenger.data.firebase.FirebaseConstants
import com.nasri.messenger.data.firebase.toUserData

interface UserService {
    fun getUserById(id: String): UserData?
    fun getUsers(query: String? = null, limit: Long = 20): List<UserData>
    fun getContactById(userId: String, contactId: String): UserData?
    fun getUserContacts(
        userId: String,
        query: String? = null,
        limit: Long = 20L
    ): List<UserData>
}

class FirebaseUserService(
    private val db: FirebaseFirestore
) : UserService {
    override fun getUserById(id: String): UserData? {
        val getUserTask = db.collection(FirebaseConstants.FIRE_COLL_USERS)
            .document(id).get()
        val documentSnapshot = Tasks.await(getUserTask)

        return if (getUserTask.isSuccessful) {
            documentSnapshot.toUserData()
        } else {
            null
        }
    }

    override fun getUsers(query: String?, limit: Long): List<UserData> {
        val getUsersTask = db.collection(FirebaseConstants.FIRE_COLL_USERS)
            .orderBy(FirebaseConstants.FIRE_DISPLAY_NAME)
            .limit(limit).get()
        val snapshots = Tasks.await(getUsersTask)
        return if (getUsersTask.isSuccessful) {
            snapshots.documents.map { it.toUserData() }
        } else {
            listOf()
        }
    }

    override fun getContactById(userId: String, contactId: String): UserData? {
        val getOneContactTask = db.collection(FirebaseConstants.FIRE_COLL_USERS)
            .document(userId).collection(FirebaseConstants.FIRE_COLL_CONTACTS)
            .document(contactId).get()
        val documentSnapshot = Tasks.await(getOneContactTask)
        return if (getOneContactTask.isSuccessful) {
            documentSnapshot.toUserData()
        } else {
            null
        }
    }

    override fun getUserContacts(
        userId: String,
        query: String?,
        limit: Long
    ): List<UserData> {
        val getContactsTask = db.collection(FirebaseConstants.FIRE_COLL_USERS)
            .document(userId).collection(FirebaseConstants.FIRE_COLL_CONTACTS)
            .orderBy(FirebaseConstants.FIRE_DISPLAY_NAME)
            .limit(limit).get()
        val documents = Tasks.await(getContactsTask)
        return if (getContactsTask.isSuccessful) {
            documents.map { it.toUserData() }
        } else {
            listOf()
        }
    }


}

class DummyUserService() : UserService {
    override fun getUserById(id: String): UserData? {
        TODO("Not yet implemented")
    }

    override fun getUsers(query: String?, limit: Long): List<UserData> {
        return RandomUsers.getRandomUsers(limit)
    }

    override fun getContactById(userId: String, contactId: String): UserData? {
        TODO("Not yet implemented")
    }

    override fun getUserContacts(
        userId: String,
        query: String?,
        limit: Long
    ): List<UserData> {
        return RandomUsers.getRandomContacts(limit)
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

fun UserData.mapToUserEntity(): UserEntity = UserEntity()



