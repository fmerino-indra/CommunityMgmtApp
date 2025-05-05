package org.fmm.communitymgmt.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import coil.ImageLoader
import coil.request.ImageRequest
import dagger.hilt.android.AndroidEntryPoint
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.databinding.ActivityHomeBinding

// FMMP: Para que esta clase pueda recibir cosas inyectadas
@AndroidEntryPoint
class HomeActivity: AppCompatActivity() {
    private lateinit var binding:ActivityHomeBinding
    private lateinit var navController: NavController

    private lateinit var iLoader: ImageLoader
    private val homeViewModel: HomeViewModel by viewModels()

//    private val userSession get() = _userSession

    // Una sola activity y varios fragments
    // Es una vista igual que un activity pero más reutilizable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        initUserSession()
        initNavigation()
        initToolBarOld()
//        initToolBarManual()
    }

    private fun initUserSession() {
//        _userInfo = userSession.userInfo!!
        iLoader = ImageLoader.Builder(baseContext).build()
        loadImageProfile()
    }

    private fun initToolBarOld()  {
        val appBarConfiguration = AppBarConfiguration(navController.graph, binding.dlCommunityListMain)

        binding.laToolbar.setupWithNavController(navController,appBarConfiguration)
        binding.mainBottomNavView.setupWithNavController(navController)

    }
    private fun initToolBarManual() {
        setSupportActionBar(binding.laToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val appBarConfiguration = AppBarConfiguration(navController.graph, binding.dlCommunityListMain)

        setupActionBarWithNavController(navController,appBarConfiguration)

        // @todo Determinar cuál usar
        binding.toolbarTitle.text = getString(R.string.community_list)
//        supportActionBar?.title = getString(R.string.community_list)



        //supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun initNavigation() {
        // Opción GPT
//        navController = findNavController(R.id.fragmentContainerView)

        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as
                NavHostFragment
        navController = navHost.navController
/*
        val appBarConfiguration = AppBarConfiguration(navController.graph, binding.dlCommunityListMain)

        binding.laToolbar.setupWithNavController(navController,appBarConfiguration)
        binding.mainBottomNavView.setupWithNavController(navController)

 */
    }
    private fun loadImageProfile() {
        if (homeViewModel.userInfo.imageUrl.isNotBlank() && homeViewModel.userInfo.imageUrl
                .isNotEmpty
                    ()) {
            val request = ImageRequest.Builder(this)
                .data(homeViewModel.userInfo.imageUrl)
                .target(binding.userInfoPhoto)
                .build()
            iLoader.enqueue(request)
        }
    }
/*
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        binding.mainNavView
        menuInflater.inflate(R.menu.)
    }

 */
}