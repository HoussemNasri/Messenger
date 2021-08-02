package com.nasri.messenger.ui.newmessage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nasri.messenger.domain.user.UserSearchResponse
import com.nasri.messenger.domain.user.UserSearchUseCase
import com.nasri.messenger.domain.user.UserSearchUseCaseParams
import com.nasri.messenger.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.nasri.messenger.domain.result.Result
import timber.log.Timber

class NewMessageViewModel(
    private val userId: String,
    private val userSearchUseCase: UserSearchUseCase
) : BaseViewModel() {
    private var searchJob: Job? = null
    private var currentQuery: String? = null

    private val _userSearchResult = MutableLiveData<Result<UserSearchResponse>>()
    val userSearchResponse: LiveData<Result<UserSearchResponse>>
        get() = _userSearchResult

    fun onSearchQuery(query: String) {
        Timber.d("onSearchQuery() called")
        if (query != currentQuery) {
            currentQuery = query
            executeSearch()
            Timber.d("User Search Started")
        }
    }

    private fun executeSearch() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            val result = userSearchUseCase(UserSearchUseCaseParams(userId, currentQuery))
            _userSearchResult.postValue(result)
        }
    }
}