package com.nasri.messenger.ui


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.nasri.messenger.R

interface DialogManager {
    fun showProgressDialog()
    fun hideProgressDialog()
    fun showNoConnectionDialog()
    fun hideNoConnectionDialog()
    fun hideAllDialogs()
}

class SimpleDialogManager(
    private val context: Context
) : DialogManager {

    private var mProgressDialog: AlertDialog =
        AlertDialog.Builder(context, R.style.progressDialog).create()
    private var mNoConnectionDialog: AlertDialog? = null


    override fun showProgressDialog() {
        val loadView: View =
            LayoutInflater.from(context).inflate(R.layout.custom_progress_dialog, null)

        mProgressDialog.setView(loadView)
        mProgressDialog.setCanceledOnTouchOutside(false)
        val tvTip: TextView = loadView.findViewById(R.id.tvTip)
        tvTip.text = context.getString(R.string.dialog_loading_label)
        mProgressDialog.show()
    }

    override fun hideProgressDialog() {
        if (mProgressDialog.isShowing) {
            mProgressDialog.dismiss()
        }
    }

    override fun showNoConnectionDialog() {
        // TODO("Not yet implemented")
    }

    override fun hideNoConnectionDialog() {
        // TODO("Not yet implemented")
    }

    override fun hideAllDialogs() {
        hideProgressDialog()
        hideNoConnectionDialog()
    }

}



