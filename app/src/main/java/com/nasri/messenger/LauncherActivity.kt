package com.nasri.messenger

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.nasri.messenger.LaunchDestination.MAIN_ACTIVITY
import com.nasri.messenger.LaunchDestination.REGISTRATION
import com.nasri.messenger.data.PreferenceStorage
import com.nasri.messenger.domain.prefs.UserLoggedInUseCase
import com.nasri.messenger.ui.registration.RegistrationActivity
import timber.log.Timber

class LauncherActivity : AppCompatActivity() {

    private lateinit var viewModelFactory: LaunchViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        viewModelFactory = LaunchViewModelFactory(UserLoggedInUseCase(PreferenceStorage()))

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