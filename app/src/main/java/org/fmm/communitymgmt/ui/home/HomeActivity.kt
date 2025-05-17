package org.fmm.communitymgmt.ui.home

import android.os.Bundle
import android.widget.Toast
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
import org.fmm.communitymgmt.ui.common.UserInfoViewModel
import org.fmm.communitymgmt.ui.enrollment.EnrollmentFragmentDirections
import org.fmm.communitymgmt.ui.home.comlist.list.CommunityListFragmentDirections
import org.fmm.communitymgmt.ui.home.comlist.list.CommunityListViewModel

// FMMP: Para que esta clase pueda recibir cosas inyectadas
@AndroidEntryPoint
class HomeActivity: AppCompatActivity() {
    private lateinit var binding:ActivityHomeBinding
    private lateinit var navController: NavController

    private lateinit var iLoader: ImageLoader
    private val userInfoViewModel: UserInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        initUserSession()
        initNavigation()
        initToolBar()
//        homeNavigate()
//        initToolBarManual()
    }

    private fun initUserSession() {
//        _userInfo = userSession.userInfo!!
        iLoader = ImageLoader.Builder(baseContext).build()
        loadImageProfile()
        binding.laToolbar.title = "probemos aqui" // nada

        // Esto saca doble texto, no vale
        //binding.toolbarTitleParticular.text = "Este es mi texto"
    }

    private fun initToolBar()  {
        val appBarConfiguration = AppBarConfiguration(navController.graph, binding.dlCommunityListMain)

        binding.laToolbar.setupWithNavController(navController,appBarConfiguration)
        binding.mainBottomNavView.setupWithNavController(navController)

    }

    private fun initNavigation() {
        // Opción GPT
//        navController = findNavController(R.id.fragmentContainerView)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.homeFragmentContainerView) as
                NavHostFragment
        navController = navHost.navController
    }

    private fun homeNavigate() {

        userInfoViewModel.userInfo.apply {
            if (community == null) {
                // To select community
                navController.navigate(
                    CommunityListFragmentDirections.actionCommunityListFragmentToCommunitySelectFragment()
                    //HomeFragmentDirections.actionHomeFragmentToCommunitySelectFragment()
                )
            } else {
                Toast.makeText(baseContext, "Nos quedamos", Toast.LENGTH_LONG).show()
                // To Community List
/*
                val aux = HomeFragmentDirections.actionHomeFragmentToCommunityListFragment()
                navController.navigate(
                    HomeFragmentDirections.actionHomeFragmentToCommunityListFragment()
                )

 */

            }
        }
    }
    private fun loadImageProfile() {
        if (userInfoViewModel.userInfo.socialUserInfo.imageUrl.isNotBlank() && userInfoViewModel
            .userInfo.socialUserInfo.imageUrl
                .isNotEmpty()) {
            val request = ImageRequest.Builder(this)
                .data(userInfoViewModel.userInfo.socialUserInfo.imageUrl)
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