package com.nasri.messenger.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nasri.messenger.data.PreferenceStorage
import com.nasri.messenger.data.SharedPreferenceStorage

abstract class BaseActivity : AppCompatActivity() {
    protected lateinit var preferenceStorage: PreferenceStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceStorage = SharedPreferenceStorage(this)
    }
}