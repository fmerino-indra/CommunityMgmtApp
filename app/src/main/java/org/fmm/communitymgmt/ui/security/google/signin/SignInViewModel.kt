package org.fmm.communitymgmt.ui.security.google.signin

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.domainlogic.usecase.GetUserInfo
import org.fmm.communitymgmt.ui.security.util.EncryptedPrefsStorage
import org.fmm.communitymgmt.util.StringResourcesProvider
import java.security.SecureRandom
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val stringResourcesProvider: StringResourcesProvider,
    private val getUserInfo: GetUserInfo,
    private val encryptedPrefsStorage: EncryptedPrefsStorage
):ViewModel() {
    private val _signInState = MutableStateFlow<LoginState>(LoginState.NotLoggedIn)
//    val signInSate: StateFlow<LoginState?> = _signInState.asStateFlow()
    val signInSate: StateFlow<LoginState> = _signInState
    private lateinit var credentialManager: CredentialManager

//    private lateinit var encryptedPrefsStorage: EncryptedPrefsStorage

    private fun onGoogleCredentialReceived(idToken: String) {
        saveEncryptedToken(idToken)

        viewModelScope.launch {
            _signInState.value = LoginState.LoggingInState(idToken)
            val result = withContext(Dispatchers.IO) {
                getUserInfo()
            }

            if (result.person != null) {
                _signInState.value = LoginState.LoggedInState(idToken, userInfo = result)
                // Usuario ya registrado

            } else {
                // Usuario no registrado
                Log.d("SignInViewModel", "Logged in but NOT registered user:")
                Log.d("SignInViewModel","$result")
                // Go To Enrollment
                _signInState.value = LoginState.NotRegisteredState(idToken, result)

            }
        }
    }

    private fun saveEncryptedToken(idToken: String) {
        encryptedPrefsStorage.saveString(
            stringResourcesProvider.getString(R.string.auth_token_key), idToken
        )
    }

    private fun onSignInError(error:Exception) {
        _signInState.value = LoginState.Error("", error)
    }

    fun initViewModel(create: CredentialManager) {
        credentialManager = create

    }

    fun launchGoogleSignIn(context: Context) {
        // Crea el EncryptedPrefsStorage()
//        encryptedPrefsStorage = EncryptedPrefsStorage(context)

        val secureRandom = SecureRandom.getInstance("SHA1PRNG")
        val nonceBytes = ByteArray(16)
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(stringResourcesProvider.getString(org.fmm.communitymgmt.R.string
                .web_server_client_id))
            .setNonce(secureRandom.nextBytes(nonceBytes).toString())
            .build()

        val credentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        viewModelScope.launch {
            try {

                val result = credentialManager.getCredential(context, credentialRequest)
                val credential = result.credential
                if (credential is GoogleIdTokenCredential) {
                    val idToken = credential.idToken
                    if (idToken.isNotEmpty()) {
                        onGoogleCredentialReceived(idToken)
                    } else {
                        onSignInError(Exception("No ID Token received"))
                    }
                }
            } catch (e: Exception) {
                onSignInError(e)
                Log.e("LoginActivity", "Error inesperado", e)
            }
        }
    }
}