package com.nasri.messenger.data.user

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.nasri.messenger.data.db.RandomUsers
import com.nasri.messenger.data.firebase.FirebaseConstants
import com.nasri.messenger.data.firebase.toUserData
import com.nasri.messenger.domain.user.User
import java.lang.IllegalStateException
import java.time.Instant
import java.util.concurrent.TimeUnit

interface UserService {
    fun getUserById(id: String): UserData?
    fun getUserByEmail(email: String): UserData?
    fun getUsers(query: String? = null, limit: Long = 20): List<UserData>
    fun getContactById(userId: String, contactId: String): UserData?
    fun getUserContacts(
        userId: String,
        query: String? = null,
        limit: Long = 20L
    ): List<UserData>

    /**
     * Create a new entry in the remote database with [userData]
     * @param userData the data to be saved
     * @return [userData] if operation succeed [null] otherwise
     * */
    suspend fun createUser(userData: UserData)
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

    override fun getUserByEmail(email: String): UserData? {
        val getUserTask = db.collection(FirebaseConstants.FIRE_COLL_USERS)
            .whereEqualTo(FirebaseConstants.FIRE_EMAIL, email).limit(1).get()
        val querySnapshot = Tasks.await(getUserTask)

        return if (getUserTask.isSuccessful && querySnapshot.size() > 0) {
            querySnapshot.documents[0].toUserData()
        } else {
            null
        }
    }

    override fun getUsers(query: String?, limit: Long): List<UserData> {
        val getUsersTask = db.collection(FirebaseConstants.FIRE_COLL_USERS)
            .orderBy(FirebaseConstants.FIRE_USERNAME)
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
            .orderBy(FirebaseConstants.FIRE_USERNAME)
            .limit(limit).get()
        val documents = Tasks.await(getContactsTask)
        return if (getContactsTask.isSuccessful) {
            documents.map { it.toUserData() }
        } else {
            listOf()
        }
    }

    override suspend fun createUser(userData: UserData) {
        val basicUserInfo = hashMapOf(
            FirebaseConstants.FIRE_USERNAME to (userData.name ?: "Jhon Doe"),
            FirebaseConstants.FIRE_PHOTO_URL to (userData.avatarUrl ?: doGenerateRandomAvatar(
                userData.id
            )),
            FirebaseConstants.FIRE_LAST_SIGN_IN to userData.lastSignedIn,
            FirebaseConstants.FIRE_EMAIL to userData.email,
            FirebaseConstants.FIRE_ACCOUNT_CREATION to Instant.now().epochSecond,
            FirebaseConstants.FIRE_PHONE to userData.phone
        )
        val createUserTask = db.collection(FirebaseConstants.FIRE_COLL_USERS).document(userData.id)
            .set(basicUserInfo)
        await(createUserTask)
        if (!createUserTask.isSuccessful) {
            throw createUserTask.exception!!
        }
    }

    private fun doGenerateRandomAvatar(seed: String?): String {
        return "https://avatars.dicebear.com/api/bottts/${seed ?: 123}.svg"
    }

    private fun await(task: Task<out Any>) {
        Tasks.await(task, 10, TimeUnit.SECONDS)
    }

}

class DummyUserService() : UserService {
    override fun getUserById(id: String): UserData? {
        TODO("Not yet implemented")
    }

    override fun getUserByEmail(email: String): UserData? {
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

    override suspend fun createUser(userData: UserData) {
        throw IllegalStateException("This is a dummy implementation, it can't create users")
    }

}

open class UserData internal constructor(
    val id: String,
    val name: String?,
    val avatarUrl: String?,
    val email: String?,
    val lastSignedIn: Long?,
    val phone: String?
) {
    data class Contact(
        val cId: String,
        val cName: String?,
        val cAvatarUrl: String?,
        val cEmail: String?,
        val cLastSignedIn: Long
    ) : UserData(cId, cName, cAvatarUrl, cEmail, cLastSignedIn, null)

    data class People(
        val pId: String,
        val pName: String?,
        val pAvatarUrl: String?,
    ) : UserData(pId, pName, pAvatarUrl, null, null, null)
}

fun UserData.mapToUser() = User(email, phone, id, name, lastSignedIn, avatarUrl)



