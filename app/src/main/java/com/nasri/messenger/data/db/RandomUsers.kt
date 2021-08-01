package com.nasri.messenger.data.db

import com.nasri.messenger.data.user.UserData
import java.util.*
import kotlin.collections.ArrayList

class RandomUsers {

    companion object {
        private val allUsers = arrayListOf(
            UserData(
                UUID.randomUUID().toString(),
                "Micheal Gonzalez",
                "https://randomuser.me/api/portraits/men/4.jpg",
                "micheal.gonzalez@example.com",
                null
            ),
            UserData(
                UUID.randomUUID().toString(),
                "Marian Hart",
                "https://randomuser.me/api/portraits/women/62.jpg",
                "marian.hart@example.com",
                null
            ),
            UserData(
                UUID.randomUUID().toString(),
                "vincent rhodes",
                "https://randomuser.me/api/portraits/men/7.jpg",
                "vincent.rhodes@example.com",
                null
            ),
            UserData(
                UUID.randomUUID().toString(),
                "steve moreno",
                "https://randomuser.me/api/portraits/men/8.jpg",
                "steve.moreno@example.com",
                null
            ),
            UserData(
                UUID.randomUUID().toString(),
                "Angel Welch",
                "https://randomuser.me/api/portraits/men/10.jpg",
                "angel.welch@example.com",
                null
            ),
            UserData(
                UUID.randomUUID().toString(),
                "Susan May",
                "https://randomuser.me/api/portraits/women/23.jpg",
                "susan.may@example.com",
                null
            ),
            UserData(
                UUID.randomUUID().toString(),
                "Joanne Brewer",
                "https://randomuser.me/api/portraits/women/26.jpg",
                "joanne.brewer@example.com",
                null
            ),
            UserData(
                UUID.randomUUID().toString(),
                "Marion Gardner",
                "https://randomuser.me/api/portraits/men/58.jpg",
                "marion.gardner@example.com",
                null
            ),
            UserData(
                UUID.randomUUID().toString(),
                "Aaron Fleming",
                "https://randomuser.me/api/portraits/men/29.jpg",
                "aaron.fleming@example.com",
                null
            ),
            UserData(
                UUID.randomUUID().toString(),
                "Mason Ford",
                "https://randomuser.me/api/portraits/men/48.jpg",
                "mason.ford@example.com",
                null
            ),
            UserData(
                UUID.randomUUID().toString(),
                "Veronica Fernandez",
                "https://randomuser.me/api/portraits/women/3.jpg",
                "veronica.fernandez@example.com",
                null
            ),
            UserData(
                UUID.randomUUID().toString(),
                "Arthur Wagner",
                "https://randomuser.me/api/portraits/men/20.jpg",
                "arthur.wagner@example.com",
                null
            ),
            UserData(
                UUID.randomUUID().toString(),
                "Nina Alvarez",
                "https://randomuser.me/api/portraits/women/12.jpg",
                "nina.alvarez@example.com",
                null
            ),
            UserData(
                UUID.randomUUID().toString(),
                "Billie Gomez",
                "https://randomuser.me/api/portraits/women/17.jpg",
                "billie.gomez@example.com",
                null
            ),
            UserData(
                UUID.randomUUID().toString(),
                "Allison Foster",
                "https://randomuser.me/api/portraits/women/24.jpg",
                "allison.foster@example.com",
                null
            ),
        )

        private val allContacts = arrayListOf(
            UserData(
                UUID.randomUUID().toString(),
                "joseph ellis",
                "https://randomuser.me/api/portraits/men/36.jpg",
                "joseph.ellis@example.com",
                null
            ),
            UserData(
                UUID.randomUUID().toString(),
                "Becky Gonzales",
                "https://randomuser.me/api/portraits/women/32.jpg",
                "becky.gonzales@example.com",
                null
            ),
            UserData(
                UUID.randomUUID().toString(),
                "Brennan Carpenter",
                "https://randomuser.me/api/portraits/women/33.jpg",
                "brennan.carpenter@example.com",
                null
            ),
            UserData(
                UUID.randomUUID().toString(),
                "Edwin Stephens",
                "https://randomuser.me/api/portraits/men/30.jpg",
                "edwin.stephens@example.com",
                null
            ),
            UserData(
                UUID.randomUUID().toString(),
                "Jason Gomez",
                "https://randomuser.me/api/portraits/men/41.jpg",
                "jason.gomez@example.com",
                null
            ),
            UserData(
                UUID.randomUUID().toString(),
                "Freddie Cook",
                "https://randomuser.me/api/portraits/men/43.jpg",
                "freddie.cook@example.com",
                null
            ),
        )

        fun getRandomUsers(limit: Long): List<UserData> {
            shuffleList(allUsers)
            if (limit > allUsers.size) {
                return allUsers
            }
            return allUsers.subList(0, limit.toInt())
        }

        fun getRandomContacts(limit: Long): List<UserData> {
            shuffleList(allContacts)
            if (limit >= allContacts.size) {
                return allContacts
            }
            return allContacts.subList(0, limit.toInt())
        }

        private fun <T> shuffleList(list: ArrayList<T>) {
            for (i in 0..20) {
                val a = Random().nextInt(list.size)
                val b = Random().nextInt(list.size)
                val temp = list[a]
                list[a] = list[b]
                list[b] = temp
            }
        }
    }


}