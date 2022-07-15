package com.borlanddev.natife_finally

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHost.navController

        navController?.also { setupActionBarWithNavController(it) }
    }
}
