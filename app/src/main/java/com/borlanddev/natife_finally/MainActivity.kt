package com.borlanddev.natife_finally

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.navigation.ui.setupActionBarWithNavController
import com.borlanddev.natife_finally.helpers.APP_PREFERENCES
import com.borlanddev.natife_finally.helpers.Prefs
import com.borlanddev.natife_finally.ui.authorization.AuthorizationFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null
    private val mainVM: MainVM by viewModels()

    @Inject
    lateinit var prefs: Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHost.navController

        if (isSignedIn()) {
            val direction =
                AuthorizationFragmentDirections.actionAuthorizationFragmentToListUsersFragment()
            navController?.navigate(
                direction,
                navOptions {
                    anim {
                        enter = R.anim.enter
                        exit = R.anim.exit
                        popEnter = R.anim.pop_enter
                        popExit = R.anim.pop_exit
                    }
                })
        } else {
            navController?.navigate(R.id.authorizationFragment)
        }

        navController?.also { setupActionBarWithNavController(it) }

        mainVM.data.observe(
            this
        ) {
            Log.d("MainActivity", "$it")
        }
    }

    override fun onSupportNavigateUp(): Boolean =
        (navController?.navigateUp() ?: false) || super.onSupportNavigateUp()

    private fun isSignedIn(): Boolean = prefs.preferences.getString(
        APP_PREFERENCES, ""
    )?.isNotEmpty() == true
}
