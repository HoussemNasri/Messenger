package com.nasri.messenger.ui.home

import android.os.Bundle
import android.view.Menu
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.nasri.messenger.R
import com.nasri.messenger.data.RegistrationUtil
import com.nasri.messenger.databinding.ActivityMainBinding
import com.nasri.messenger.ui.base.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar2))


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
            Timber.d("Menu Clicked")
            when (it.itemId) {
                R.id.nav_logout -> {
                    // TODO ('Logout user from all providers and cleanup user data')
                    GlobalScope.launch(Dispatchers.Main) {
                        RegistrationUtil.signOutAllProviders(this@MainActivity)
                        navController.navigate(R.id.action_chatsFragment_to_registrationActivity)
                        finish()
                        Timber.d("Logout")
                    }
                }
            }
            false
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        // TODO (disable up navigation by using links instead.)
        return findNavController(R.id.main_nav_host_fragment).navigateUp(appBarConfiguration)
    }


}