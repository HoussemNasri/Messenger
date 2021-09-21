package com.nasri.messenger.ui.home

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nasri.messenger.R
import com.nasri.messenger.data.SharedPreferenceStorage
import com.nasri.messenger.data.user.FirebaseAuthRepository
import com.nasri.messenger.data.user.FirebaseUserService
import com.nasri.messenger.data.user.UserRepositoryImpl
import com.nasri.messenger.databinding.ActivityMainBinding
import com.nasri.messenger.databinding.NavHeaderMainBinding
import com.nasri.messenger.domain.result.EventObserver
import com.nasri.messenger.ui.base.BaseActivity
import timber.log.Timber
import com.bumptech.glide.load.engine.DiskCacheStrategy

import android.graphics.drawable.PictureDrawable
import android.net.Uri
import com.nasri.messenger.ui.glide.GlideApp
import com.nasri.messenger.ui.glide.MessengerModule


class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHeaderBinding: NavHeaderMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private val viewModel: MainActivityViewModel by viewModels {
        val auth = FirebaseAuth.getInstance()
        val userService = FirebaseUserService(FirebaseFirestore.getInstance())
        val userRepository = UserRepositoryImpl(userService)
        val authRepository = FirebaseAuthRepository(
            auth, userRepository, SharedPreferenceStorage(applicationContext)
        )
        MainActivityViewModelFactory(authRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        navHeaderBinding =
            NavHeaderMainBinding.bind(binding.navView.getHeaderView(0))
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar2.root)
        setLogoutMenuItemTextColorToRed()

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment? ?: return

        navController = host.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.chatsFragment),
            binding.drawerLayout
        )

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)


        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_logout -> {
                    viewModel.signOut()
                }
            }
            false
        }

        viewModel.navigationActions.observe(this, EventObserver {
            when (it) {
                is MainNavigationAction.NavigateToRegistrationActivityAction -> {
                    navController.navigate(R.id.action_chatsFragment_to_registrationActivity)
                }
            }
            finish()
        })

        viewModel.currentUser.observe(this, {
            navHeaderBinding.navUserName.text = it.username
            navHeaderBinding.navUserEmail.text = it.email

            GlideApp.with(this)
                .load(it.photoUrl)
                .into(navHeaderBinding.navUserAvatar)

        })

    }

    private fun setLogoutMenuItemTextColorToRed() {
        val logoutItem = binding.navView.menu.findItem(R.id.nav_logout)
        val spannable = SpannableString(logoutItem.title.toString())
        spannable.setSpan(ForegroundColorSpan(Color.parseColor("#F03A3A")), 0, spannable.length, 0)
        logoutItem.title = spannable
    }

    override fun onSupportNavigateUp(): Boolean {
        // TODO (disable up navigation by using links instead.)
        return findNavController(R.id.main_nav_host_fragment).navigateUp(appBarConfiguration)
    }


}