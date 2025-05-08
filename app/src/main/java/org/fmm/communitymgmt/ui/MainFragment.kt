package org.fmm.communitymgmt.ui

import android.accounts.AccountManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.credentials.CredentialManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.fmm.communitymgmt.databinding.FragmentMainBinding
import org.fmm.communitymgmt.ui.enrollment.EnrollmentActivity
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
                        SignInState.NotLoggedIn -> notLoggedInState()
                        is SignInState.LoggingInState -> loggingInState(it)
                        is SignInState.LoggedInState -> loggedInState(it)
                        is SignInState.NotRegisteredState -> notRegisteredState(it)
                        is SignInState.RegisteringState -> registeringState(it)
                        is SignInState.NotActivatedState -> notActivatedState(it)
                        is SignInState.Error -> errorState()
                        SignInState.NotCredentialsState -> noCredentials()
                    }
                }
            }
        }
    }

    private fun notActivatedState(it: SignInState.NotActivatedState) {
        Log.d(this::class.simpleName, "Navigating to BrothersEnrollmentFragment")
        val intent = Intent(requireContext(), EnrollmentActivity::class.java).apply {
            putExtra("targetFragment", "BrothersEnrollmentFragment")
        }
        startActivity(intent)
    }

    private fun registeringState(it: SignInState.RegisteringState) {
        // No puedo usar FragmentDirections en este caso, porque quiero ir a un fragment
        // diferente del por defecto de la actividad, y si son gráficos diferentes no deja.
//        findNavController().navigate(
//            MainFragmentDirections.actionMainFragmentToEnrollmentActivity()
//        )
        // Se hace así:
        val intent = Intent(requireContext(), EnrollmentActivity::class.java).apply {
            putExtra("targetFragment", "CommunityEnrollmentFragment")
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

    private fun notRegisteredState(it: SignInState.NotRegisteredState) {
        findNavController().navigate(
            // Esta clase es autogenerada por NavArgs
            // El método se crea cuando se hace el enganche en el graph. También se ha añadido un argumento que se pasa aquí, y se recibe en el activity
            MainFragmentDirections.actionMainFragmentToEnrollmentActivity()
//            MainFragmentDirections.actionMainFragmentToHomeActivity()
        )
    }

    private fun notLoggedInState() {
        binding.pb.isVisible = false
    }

    private fun loggingInState(state: SignInState.LoggingInState) {
        Log.d("LoginFragment", state.idToken)
        binding.pb.isVisible = true
        // Hay que poner el ProgressBar a visible para que luego llame a userInfo
        //startActivity(Intent(activity, HomeActivity::class.java))
        /*
                findNavController().navigate(
                    // Esta clase es autogenerada por NavArgs
                    // El método se crea cuando se hace el enganche en el graph. También se ha añadido un argumento que se pasa aquí, y se recibe en el activity
                    LoginFragmentDirections.actionLoginFragmentToCommunityListGraph()
                )
        */
    }
    private fun loggedInState(it: SignInState.LoggedInState) {
        // Navegar a: CommunityList
        // Quitar progress bar
        // Guardar cosas en EncryptedPrefsStorage
        // No sé si pintar en algún sitio la foto
        Log.d(this::class.simpleName, it.userInfo.toString())
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToHomeActivity()
        )

    }


    private fun errorState() {
    }



}