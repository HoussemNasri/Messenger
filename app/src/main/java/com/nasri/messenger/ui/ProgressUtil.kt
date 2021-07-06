package com.nasri.messenger.ui


import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.nasri.messenger.R


/**
 * Time consuming dialog tool class
 */
object ProgressDialogUtil {
    private var mAlertDialog: AlertDialog? = null

    /**
     * Pop-up timeout dialog
     * @param context
     */
    fun showProgressDialog(context: Context?) {
        if (mAlertDialog == null) {
            mAlertDialog = AlertDialog.Builder(context!!, R.style.progressDialog).create()
        }
        val loadView: View =
            LayoutInflater.from(context).inflate(R.layout.custom_progress_dialog, null)
        mAlertDialog!!.setView(loadView, 0, 0, 0, 0)
        mAlertDialog!!.setCanceledOnTouchOutside(false)
        val tvTip: TextView = loadView.findViewById(R.id.tvTip)
        tvTip.text = "Loading..."
        mAlertDialog!!.show()
    }

    fun showProgressDialog(context: Context?, tip: String?) {
        var tip = tip
        if (TextUtils.isEmpty(tip)) {
            tip = "Loading..."
        }
        if (mAlertDialog == null) {
            mAlertDialog = AlertDialog.Builder(context!!, R.style.progressDialog).create()
        }
        val loadView: View =
            LayoutInflater.from(context).inflate(R.layout.custom_progress_dialog, null)
        mAlertDialog!!.setView(loadView, 0, 0, 0, 0)
        mAlertDialog!!.setCanceledOnTouchOutside(false)
        val tvTip: TextView = loadView.findViewById(R.id.tvTip)
        tvTip.text = "Loading..."
        mAlertDialog!!.show()
    }

    /**
     * Hide time-consuming dialogs
     */
    fun dismiss() {
        if (mAlertDialog != null && mAlertDialog!!.isShowing()) {
            mAlertDialog!!.dismiss()
        }
    }
}
