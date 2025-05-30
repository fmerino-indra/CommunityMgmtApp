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
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.AndroidEntryPoint
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.databinding.ActivityEnrollmentBinding
import org.fmm.communitymgmt.databinding.ActivityMainBinding
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import org.fmm.communitymgmt.ui.common.EnrollmentStates
import org.fmm.communitymgmt.ui.common.UserInfoViewModel
import org.fmm.communitymgmt.ui.enrollment.signup.SignUpFormState
import org.fmm.communitymgmt.ui.enrollment.signup.SignUpViewModel
import org.fmm.communitymgmt.ui.security.model.CredentialsData

@AndroidEntryPoint
class EnrollmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEnrollmentBinding
    private lateinit var navController: NavController

    private val userInfoViewModel: UserInfoViewModel by viewModels()
    private lateinit var userInfo: UserInfoModel
    private lateinit var credential: CredentialsData
    private lateinit var iLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnrollmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        initUserInfoViewModel()
        initUserSession()
        initNavigation()
//        initToolBarManual()
    }

    private fun initUserInfoViewModel() {
        if (userInfoViewModel.userSession.isLoggedIn())
            credential = userInfoViewModel.userSession.credential
        if (userInfoViewModel.userSession.isFullLoggedIn())
            userInfo = userInfoViewModel.userSession.userInfo
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
    }

    private fun loadImageProfile() {
        if (credential.profilePictureUri != null) {
            val request = ImageRequest.Builder(this)
//                .data(userInfoViewModel.userSession.userInfo.socialUserInfo.imageUrl)
                .data(credential.profilePictureUri)
                .target(binding.userInfoPhoto)
                .build()
            iLoader.enqueue(request)
        }
    }

    private fun enrollmentNavigate(isResponsible: Boolean) {
        val state= intent.getIntExtra(getString(R.string.enrollmentState),-1)
        var fragmentRef: Int? = null

        if (isResponsible) {
            fragmentRef = when(state) {
                EnrollmentStates.NOTENROLLED.id -> R.id.signUpFragment
                EnrollmentStates.NOTCOMMUNITY.id -> R.id.communityEnrollmentFragment
                EnrollmentStates.FULLENROLLED.id -> R.id.brothersEnrollmentFragment
                else -> {
                    -1
                }
            }
        } else {
            fragmentRef = when(state) {
                EnrollmentStates.NOTENROLLED.id -> R.id.signUpFragment
                EnrollmentStates.NOTCOMMUNITY.id -> R.id.invitationFragment
                EnrollmentStates.FULLENROLLED.id -> R.id.invitationFragment // Cuando la comunidad
                // no está activada
                else -> {
                    -1
                }
            }
        }
        if (fragmentRef>=-1) {
            if (state == EnrollmentStates.NOTENROLLED.id) {
                navController.navigate(
                    EnrollmentFragmentDirections
                        .actionEnrollmentFragmentToSignUpFragment(isResponsible)
                )
            } else {
                navController.navigate(fragmentRef)
            }
        } else {
            // Tratar error
        }
    }
    private fun askResponsible() {
        val dialog = YesNoBottomSheetDialogFragment(getString(R.string.isResponsible)) {
            isResponsible ->

            enrollmentNavigate(isResponsible)
        }
        dialog.show(supportFragmentManager, getString(R.string.important_question))
    }

}