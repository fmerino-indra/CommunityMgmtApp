package org.fmm.communitymgmt.ui.enrollment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.BindingAdapter
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
        redirectToCommunityEnrollment()
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
        }

    }
/*
    /**
     * Bindings
     */
    //    fun bindName(view: EditText, state: EditPersonFormState?, callback: ((String) -> Unit)) {
    companion object {

        @BindingAdapter("viewModelFieldValue", requireAll = true)
        @JvmStatic
        fun bindName(
            view: EditText, state: SignUpFormState?
        ) {
            if (state == null) return
            if (view.text.toString() != state.name) {
                view.setText(state.name)
            }
        }
    }

 */
}