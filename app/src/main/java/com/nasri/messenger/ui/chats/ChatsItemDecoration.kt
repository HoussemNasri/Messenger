package com.nasri.messenger.ui.chats

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DividerItemDecoration
import com.nasri.messenger.R

class ChatsItemDecoration(val context: Context, orientation: Int = VERTICAL) :
    DividerItemDecoration(context, orientation) {
    init {
        AppCompatResources.getDrawable(
            context,
            R.drawable.layer
        )?.let { setDrawable(it) }
    }
}
