package com.nasri.messenger.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nasri.messenger.data.PreferenceStorage
import com.nasri.messenger.data.SharedPreferenceStorage
import com.nasri.messenger.ui.DialogManager
import com.nasri.messenger.ui.SimpleDialogManager

abstract class BaseActivity : AppCompatActivity() {
    protected lateinit var preferenceStorage: PreferenceStorage
    protected lateinit var dialogManager: DialogManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceStorage = SharedPreferenceStorage(this)
        dialogManager = SimpleDialogManager(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        dialogManager.hideAllDialogs()
    }

}