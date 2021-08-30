package com.nasri.messenger.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.nasri.messenger.ui.DialogManager
import com.nasri.messenger.ui.SimpleDialogManager
import com.nasri.messenger.data.PreferenceStorage
import com.nasri.messenger.data.SharedPreferenceStorage

open class BaseFragment : Fragment() {

    protected lateinit var preferenceStorage: PreferenceStorage
    protected lateinit var dialogManager: DialogManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceStorage = SharedPreferenceStorage(requireContext())
        dialogManager = SimpleDialogManager(requireContext())

    }

    override fun onDestroy() {
        super.onDestroy()
        dialogManager.hideAllDialogs()
    }

    override fun onPause() {
        super.onPause()
        dialogManager.hideAllDialogs()
    }
}