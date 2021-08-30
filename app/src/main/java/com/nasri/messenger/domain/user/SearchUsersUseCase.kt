package com.nasri.messenger.domain.user

import com.nasri.messenger.data.user.UserRepository
import com.nasri.messenger.domain.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import java.util.*

data class UserSearchUseCaseParams(
    val userId: String,
    val query: String?,
    val onlyFetchContacts: Boolean = false,
    val contactsLimit: Long = 10,
    val peopleLimit: Long = 10,
)

data class UserSearchResponse(
    val contacts: List<User>,
    val people: List<User>,
)

class SearchUsersUseCase(
    private val userRepository: UserRepository
) : UseCase<UserSearchUseCaseParams, UserSearchResponse>(Dispatchers.IO) {
    override suspend fun execute(parameters: UserSearchUseCaseParams): UserSearchResponse {
        delay(5000)
        val contacts = userRepository.findUserContacts(
            parameters.query,
            parameters.userId,
            parameters.contactsLimit
        )
        val people: List<User> =
            if (parameters.onlyFetchContacts) Collections.emptyList()
            else userRepository.findUsers(parameters.query, parameters.peopleLimit)

        return UserSearchResponse(contacts, people)
    }
}