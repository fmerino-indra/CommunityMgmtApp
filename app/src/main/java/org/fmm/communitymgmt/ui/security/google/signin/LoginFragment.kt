package org.fmm.communitymgmt.ui.security.google.signin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.credentials.CredentialManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.fmm.communitymgmt.databinding.FragmentLoginBinding

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignInViewModel by viewModels<SignInViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
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
                        LoginState.NotLoggedIn -> notLoggedInState()
                        is LoginState.LoggingInState -> loggingInState(it)
                        is LoginState.LoggedInState -> loggedInState(it)
                        is LoginState.Error -> errorState()
                        is LoginState.NotRegisteredState -> notRegisteredState(it)
                    }
                }
            }
        }
    }

    private fun notRegisteredState(it: LoginState.NotRegisteredState) {
        findNavController().navigate(
            // Esta clase es autogenerada por NavArgs
            // El método se crea cuando se hace el enganche en el graph. También se ha añadido un argumento que se pasa aquí, y se recibe en el activity
            LoginFragmentDirections.actionLoginFragmentToSignUpFragment(it.userInfo)
        )

    }

    private fun notLoggedInState() {

    }

    private fun loggingInState(state: LoginState.LoggingInState) {
        Log.d("LoginFragment", state.idToken)
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
    private fun loggedInState(it: LoginState.LoggedInState) {
        // Navegar a: CommunityList
        // Quitar progress bar
        // Guardar cosas en EncryptedPrefsStorage
        // No sé si pintar en algún sitio la foto
        Log.d(this::class.toString(), it.userInfo.toString())
    }


    private fun errorState() {
    }


}