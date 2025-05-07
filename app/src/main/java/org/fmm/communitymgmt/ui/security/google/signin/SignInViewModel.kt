package org.fmm.communitymgmt.ui.security.google.signin

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import io.fusionauth.jwt.JWTDecoder
import io.fusionauth.jwt.JWTUtils
import io.fusionauth.jwt.domain.JWT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.domainlogic.usecase.GetUserInfo
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import org.fmm.communitymgmt.ui.security.model.UserSession
import org.fmm.communitymgmt.ui.security.util.EncryptedPrefsStorage
import org.fmm.communitymgmt.util.StringResourcesProvider
import java.security.SecureRandom
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val stringResourcesProvider: StringResourcesProvider,
    private val getUserInfo: GetUserInfo,
    private val encryptedPrefsStorage: EncryptedPrefsStorage,
    private val userSession: UserSession
):ViewModel() {
    private val _signInState = MutableStateFlow<SignInState>(SignInState.NotLoggedIn)
//    val signInSate: StateFlow<LoginState?> = _signInState.asStateFlow()
    val signInSate: StateFlow<SignInState> = _signInState
    private lateinit var credentialManager: CredentialManager

//    private lateinit var encryptedPrefsStorage: EncryptedPrefsStorage

    private fun processJwt(idToken: String) {
        val jwt = JWTUtils.decodePayload(idToken)
        jwt.getString("email")
        Log.d("SignInViewModel", "$jwt")
    }
    private fun onGoogleCredentialReceived(idToken: String) {
        saveEncryptedToken(idToken)
        processJwt(idToken)
// @todo Extraer información del idToken
        viewModelScope.launch {
            _signInState.value = SignInState.LoggingInState(idToken)
            val result: UserInfoModel = withContext(Dispatchers.IO) {
                getUserInfo()
            }
            userSession.userInfo = result
            if (result.isFullEnrolled()) {
                // Usuario ya registrado
                _signInState.value = SignInState.LoggedInState(idToken, userInfo = result)
            } else if (result.isRegistering()) {
                Log.d("SignInViewModel", "Registered user but NOT community:")
                _signInState.value = SignInState.RegisteringState(idToken, userInfo = result)
            } else {
                // Usuario no registrado
                Log.d("SignInViewModel", "Logged in but NOT registered user:")
                Log.d("SignInViewModel","$result")
                // Go To Enrollment
                _signInState.value = SignInState.NotRegisteredState(idToken, result)
            }
        }
    }

    private fun saveEncryptedToken(idToken: String) {
        encryptedPrefsStorage.saveString(
            stringResourcesProvider.getString(R.string.auth_token_key), idToken
        )
    }

    private fun onSignInError(error:Exception) {
        _signInState.value = SignInState.Error("", error)
    }

    private fun onNoCredentials() {
        _signInState.value = SignInState.NotCredentialsState
    }
    fun initViewModel(cManager: CredentialManager) {
        credentialManager = cManager
        //@todo Estoy hay que coordinarlo con ManagerCredential de google, en la forma de login
        // automático
        if (userSession.isLoggedIn()) {

        } else {

        }
    }

    fun launchGoogleSignIn(context: Context) {
        // Crea el EncryptedPrefsStorage()
//        encryptedPrefsStorage = EncryptedPrefsStorage(context)

        val secureRandom = SecureRandom.getInstance("SHA1PRNG")
        val nonceBytes = ByteArray(16)
        var googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(stringResourcesProvider.getString(R.string
                .web_server_client_id))
            .setNonce(secureRandom.nextBytes(nonceBytes).toString())
            .build()

/*
        val credentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
*/
        viewModelScope.launch {
            try {
                tryCredentials(context, googleIdOption)
/*
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

 */         } catch (nce: NoCredentialException) {
                googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(stringResourcesProvider.getString(R.string
                        .web_server_client_id))
                    .setNonce(secureRandom.nextBytes(nonceBytes).toString())
                    .build()
                try {
                    tryCredentials(context, googleIdOption)
                } catch (nce2: NoCredentialException) {
                    Log.d("SignInViewModel", "Must navigate to SignUp")
                    onNoCredentials()
                }
            } catch (e: Exception) {
                onSignInError(e)
                Log.e("LoginActivity", "Error inesperado", e)
            }
        }
    }

    private suspend fun tryCredentials(context: Context,googleIdOption: GetGoogleIdOption ) {
        val credentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(context, credentialRequest)
        var credential = result.credential

        // A veces devuelve CustomCredential
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential
                .TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            credential = GoogleIdTokenCredential.createFrom(credential.data)

        }

        if (credential is GoogleIdTokenCredential) {
            val idToken = credential.idToken
            if (idToken.isNotEmpty()) {
                onGoogleCredentialReceived(idToken)
            } else {
                onSignInError(Exception("No ID Token received"))
            }
        }

    }
}