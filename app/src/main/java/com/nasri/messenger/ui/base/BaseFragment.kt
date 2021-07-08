package com.nasri.messenger.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.nasri.messenger.data.PreferenceStorage
import com.nasri.messenger.data.SharedPreferenceStorage
import com.nasri.messenger.ui.ProgressDialogUtil

open class BaseFragment : Fragment() {

    protected lateinit var preferenceStorage: PreferenceStorage

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceStorage = SharedPreferenceStorage(requireContext())

    }

    fun showProgress() {
        ProgressDialogUtil.showProgressDialog(requireContext())
    }

    fun hideProgress() {
        ProgressDialogUtil.dismiss()
    }

    fun showConnectivityError() {

    }

    override fun onDestroy() {
        super.onDestroy()
        hideProgress()
    }

    override fun onPause() {
        super.onPause()
        hideProgress()
    }
}