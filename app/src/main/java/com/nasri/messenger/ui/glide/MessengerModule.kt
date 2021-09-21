package com.nasri.messenger.ui.glide

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule


@GlideModule
class MessengerModule : AppGlideModule() {
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}