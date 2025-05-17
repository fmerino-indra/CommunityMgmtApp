package org.fmm.communitymgmt.ui

import android.accounts.AccountManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.credentials.CredentialManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.databinding.FragmentMainBinding
import org.fmm.communitymgmt.ui.common.EnrollmentStates
import org.fmm.communitymgmt.ui.enrollment.EnrollmentActivity
import org.fmm.communitymgmt.ui.home.HomeActivity
import org.fmm.communitymgmt.ui.security.google.signin.SignInState
import org.fmm.communitymgmt.ui.security.google.signin.SignInViewModel


@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignInViewModel by viewModels<SignInViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initUI()
    }

    private fun initData() {
        viewModel.initViewModel(CredentialManager.create(requireContext()))
    }

    private fun initUI() {
        initUIState()
        initListeners()
    }

    private fun initListeners() {
        binding.googleSignInButton.setOnClickListener {
            viewModel.launchGoogleSignIn(requireContext())
        }
    }

    private fun initUIState() {
        // Esta se engancha al ciclo de vida del fragmento
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.signInSate.collect{
                    when(it) {
                        // Inner states
                        SignInState.NotLoggedIn -> notLoggedInState()
                        is SignInState.LoggingInState -> loggingInState()
                        is SignInState.Error -> errorState()
                        SignInState.NotCredentialsState -> noCredentials()
                        // Navigation states
                        SignInState.NotRegisteredState -> navigateToEnrollment(it)
                        SignInState.RegisteringState -> navigateToEnrollment(it)
                        SignInState.NotActivatedState -> navigateToEnrollment(it)
                        is SignInState.LoggedInState -> loggedInState(it)
                    }
                }
            }
        }
    }

    private fun loggedInState(it: SignInState.LoggedInState) {
        // Navegar a: CommunityList
        // Quitar progress bar
        // Guardar cosas en EncryptedPrefsStorage
        // No sé si pintar en algún sitio la foto
        Log.d(this::class.simpleName, "Navigating to Community List with user: ${it.userInfo
            .socialUserInfo.name}")
/*
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.main_graph, inclusive = true)
            .build()
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToHomeActivity(),
            navOptions
        )
 */
        /*
        Aunque funciona la navegación hacia adelante de este fragment a la HomeActivity, si lo
        hago con Navigation Component falla la marcha atrás

         */

        val intent = Intent(requireContext(), HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun navigateToEnrollment(it: SignInState) {
        Log.d(this::class.simpleName, "Navigating to ${it.state?.name}")
        val intent = Intent(requireContext(), EnrollmentActivity::class.java).apply {
            putExtra(getString(R.string.enrollmentState), it.state?.id)
        }
        startActivity(intent)
    }

    private fun noCredentials() {
        binding.pb.isVisible = false

        // Launch the Google account setup
        val accountManager = AccountManager.get(requireContext())
        accountManager.addAccount(
            "com.google", "Google Account", null, Bundle(),
            requireActivity(), null, null
        )
    }

    private fun notLoggedInState() {
        binding.pb.isVisible = false
    }

    private fun loggingInState() {
        binding.pb.isVisible = true
    }

    private fun errorState() {
        binding.pb.isVisible = false

        Toast.makeText(
            requireContext(), getString(R.string.loginException), Toast.LENGTH_LONG
        ).show()
    }



}