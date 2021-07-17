package com.nasri.messenger.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    private val _showProgress: MutableLiveData<Boolean> = MutableLiveData()
    val showProgress: LiveData<Boolean>
        get() = _showProgress

    fun hideProgress() {
        _showProgress.postValue(false)
    }

    fun showProgress() {
        _showProgress.postValue(true)
    }

}