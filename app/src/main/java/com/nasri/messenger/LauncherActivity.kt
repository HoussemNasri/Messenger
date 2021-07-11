package com.nasri.messenger

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import com.nasri.messenger.LaunchDestination.MAIN_ACTIVITY
import com.nasri.messenger.LaunchDestination.REGISTRATION
import com.nasri.messenger.data.RegistrationUtil
import com.nasri.messenger.data.UserInfoGsonAdapter
import com.nasri.messenger.domain.UriTypeAdapter
import com.nasri.messenger.domain.prefs.UserLoggedInUseCase
import com.nasri.messenger.ui.base.BaseActivity
import com.nasri.messenger.ui.home.MainActivity
import com.nasri.messenger.ui.registration.RegistrationActivity
import timber.log.Timber


class LauncherActivity : BaseActivity() {

    private lateinit var viewModelFactory: LaunchViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        if (preferenceStorage.isUserLoggedIn) {
            val gson = GsonBuilder()
                .registerTypeAdapter(Uri::class.java, UriTypeAdapter())
                .create()
            val providerData =
                FirebaseAuth.getInstance().currentUser?.providerData?.map { UserInfoGsonAdapter(it) }
            val providerDataJson = gson.toJson(providerData)
            Timber.d(providerDataJson)
            Timber.d(preferenceStorage.getCurrentUserInfo()?.getProviderData()?.get(0)?.photoUrl.toString())
        }

        // TODO (Just for testing)
        RegistrationUtil.signOutAllProviders(this)

        viewModelFactory =
            LaunchViewModelFactory(UserLoggedInUseCase(preferenceStorage))

        val viewModel: LaunchViewModel by viewModels { viewModelFactory }

        viewModel.launchDestination.observe(this, { destination ->
            when (destination!!) {
                REGISTRATION -> startActivity(Intent(this, RegistrationActivity::class.java))
                MAIN_ACTIVITY -> startActivity(Intent(this, MainActivity::class.java))
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        })
    }

}