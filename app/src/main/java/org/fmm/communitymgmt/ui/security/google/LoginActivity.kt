package org.fmm.communitymgmt.ui.security.google

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.PublicKeyCredential
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.fmm.communitymgmt.databinding.ActivityLoginBinding
import org.json.JSONObject
import java.security.SecureRandom

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var credentialManager: CredentialManager
    private val viewModel: SignInViewModel by viewModels()
    companion object {
        const val RC_SIGN_IN = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        credentialManager = CredentialManager.create(this)
        initUI()
    }

    private fun initUI() {
        initListeners()
        initUIState()
    }

    private fun initListeners() {
        binding.googleSignInButton.setOnClickListener {
            launchGoogleSignIn()
        }
    }
    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.signInSate.collectLatest {
                    it?.let { Log.d("SignIn", it) }
                }
            }
        }
    }

    private fun launchGoogleSignIn() {
        val secureRandom = SecureRandom.getInstance("SHA1PRNG")
        var nonceBytes = ByteArray(16)
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(org.fmm.communitymgmt.R.string.web_server_client_id))
//            .setNonce(secureRandom.nextBytes(nonceBytes).toString())
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {

                val result = credentialManager.getCredential(this@LoginActivity, request)
                val credential = result.credential
                if (credential is PublicKeyCredential) {
                    val responseJson = credential.authenticationResponseJson
                    val jsonObject = JSONObject(responseJson)
                    val idToken = jsonObject.optString("id_token")

                    if (!idToken.isNullOrEmpty()) {
                        viewModel.onGoogleCredentialReceived(idToken)
                    } else {
                        viewModel.onSignInError("No ID Token received")
                    }
                } else if (credential is GoogleIdTokenCredential) {
                    val idToken = credential.idToken
                    if (idToken.isNotEmpty()) {
                        viewModel.onGoogleCredentialReceived(idToken)
                    } else {
                        viewModel.onSignInError("No ID Token received")
                    }
                }
            } catch (e: Exception) {
                viewModel.onSignInError(e.localizedMessage ?: "Unknown error")
                Log.e("LoginActivity", "Error inesperado", e)
            }
        }
    }
}