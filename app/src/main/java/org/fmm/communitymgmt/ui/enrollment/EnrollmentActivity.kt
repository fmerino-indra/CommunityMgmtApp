package org.fmm.communitymgmt.ui.enrollment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import coil.ImageLoader
import coil.request.ImageRequest
import dagger.hilt.android.AndroidEntryPoint
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.databinding.ActivityEnrollmentBinding
import org.fmm.communitymgmt.databinding.ActivityMainBinding
import org.fmm.communitymgmt.ui.common.UserInfoViewModel
import org.fmm.communitymgmt.ui.enrollment.signup.SignUpFormState
import org.fmm.communitymgmt.ui.enrollment.signup.SignUpViewModel

@AndroidEntryPoint
class EnrollmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEnrollmentBinding
    private lateinit var navController: NavController

    private val userInfoViewModel: UserInfoViewModel by viewModels()
    private lateinit var iLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnrollmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        initUserSession()
        initNavigation()
//        initToolBarManual()
    }

    private fun initUserSession() {
        // Obligatorio para LiveData
        binding.lifecycleOwner = this
        binding.userInfoViewModel = userInfoViewModel

        iLoader = ImageLoader.Builder(baseContext).build()
        loadImageProfile()
    }

    private fun initNavigation() {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.enrollmentFragmentContainerView) as
                    NavHostFragment
        navController = navHost.navController
        askResponsible()
//        redirectToCommunityEnrollment()
    }

    private fun loadImageProfile() {
        if (userInfoViewModel.userInfo.imageUrl.isNotBlank() && userInfoViewModel.userInfo.imageUrl
                .isNotEmpty
                    ()
        ) {
            val request = ImageRequest.Builder(this)
                .data(userInfoViewModel.userInfo.imageUrl)
                .target(binding.userInfoPhoto)
                .build()
            iLoader.enqueue(request)
        }
    }

    private fun redirectToCommunityEnrollment() {

        val target = intent.getStringExtra("targetFragment")
        if (target == "CommunityEnrollmentFragment") {
            navController.navigate(R.id.communityEnrollmentFragment)
        } else if (target == "BrothersEnrollmentFragment") {
            navController.navigate(R.id.brothersEnrollmentFragment)
        }

    }

    private fun askResponsible() {
        val dialog = YesNoBottomSheetDialogFragment(getString(R.string.isResponsible)) {
            isResponsible ->
            if (isResponsible) {
                redirectToCommunityEnrollment()
            } else {
                redirectToWaitingInvitation()
            }
        }
        dialog.show(supportFragmentManager, getString(R.string.important_question))
    }

    private fun redirectToWaitingInvitation() {
        navController.navigate(R.id.QRReaderFragment)
    }
}