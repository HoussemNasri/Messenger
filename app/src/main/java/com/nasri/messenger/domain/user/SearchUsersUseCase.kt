package com.nasri.messenger.domain.user

import com.nasri.messenger.data.user.ContactRepository
import com.nasri.messenger.data.user.PeopleRepository
import com.nasri.messenger.domain.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

data class UserSearchUseCaseParams(
    val userId: String,
    val query: String?,
)

data class UserSearchResponse(
    val contacts: List<com.nasri.messenger.data.user.UserData.Contact>,
    val people: List<com.nasri.messenger.data.user.UserData.People>
)

class SearchUsersUseCase(
    private val contactRepository: ContactRepository,
    private val peopleRepository: PeopleRepository
) : UseCase<UserSearchUseCaseParams, UserSearchResponse>(Dispatchers.IO) {
    override suspend fun execute(parameters: UserSearchUseCaseParams): UserSearchResponse {
        delay(5000)
        val contacts = contactRepository.getContacts(parameters.query, 10)
        val people = peopleRepository.getPeople(parameters.query, 10)
        return UserSearchResponse(contacts, people)
    }
}