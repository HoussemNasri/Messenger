package com.nasri.messenger.ui.newmessage

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nasri.messenger.data.user.ContactRepository
import com.nasri.messenger.data.user.PeopleRepository
import com.nasri.messenger.data.user.UserData

class UserSuggestionPagingSource(
    private val contactRepository: ContactRepository,
    private val peopleRepository: PeopleRepository
) : PagingSource<Int, UserData>() {
    override fun getRefreshKey(state: PagingState<Int, UserData>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserData> {
        TODO("Not yet implemented")
    }

}