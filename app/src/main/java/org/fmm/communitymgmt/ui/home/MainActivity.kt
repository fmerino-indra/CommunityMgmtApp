package org.fmm.communitymgmt.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.databinding.ActivityMainBinding

// FMMP: Para que esta clase pueda recibir cosas inyectadas
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var navController: NavController

    // Una sola activity y varios fragments
    // Es una vista igual que un activity pero más reutilizable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        initNavigation()
        initToolBar()
    }

    private fun initToolBar() {
        //supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun initNavigation() {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as
                NavHostFragment
        navController = navHost.navController

        val appBarConfiguration = AppBarConfiguration(navController.graph, binding.dlCommunityListMain)

        binding.laToolbar.setupWithNavController(navController,appBarConfiguration)
        binding.mainBottomNavView.setupWithNavController(navController)
    }
}