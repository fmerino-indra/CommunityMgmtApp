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
import io.fusionauth.jwt.JWTUtils
import io.fusionauth.jwt.domain.JWT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.domainlogic.exceptions.SocialUserNotFoundException
import org.fmm.communitymgmt.domainlogic.usecase.GetUserInfo
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import org.fmm.communitymgmt.ui.security.model.CredentialsData
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
    //,
    //private val userSession: UserSession
):ViewModel() {
    private val _signInState = MutableStateFlow<SignInState>(SignInState.NotLoggedIn)
//    val signInSate: StateFlow<LoginState?> = _signInState.asStateFlow()
    val signInSate: StateFlow<SignInState> = _signInState
    private lateinit var credentialManager: CredentialManager
//    private lateinit var userInfoViewModel: UserInfoViewModel

    // @TODO Revisar si es necesario referencia el UserInfoViewModel
//    fun initData(userInfoViewModel: UserInfoViewModel) {
//        this.userInfoViewModel = userInfoViewModel
//    }

    private fun processJwt(idToken: String) {
        val jwt = JWTUtils.decodePayload(idToken)
        jwt.getString("email")
        Log.d("SignInViewModel", "$jwt")
    }

    private fun whatSate(userInfo:UserInfoModel):SignInState {
        return if (userInfo.myCommunities.isEmpty()) {
            SignInState.RegisteringState
        } else if (userInfo.selectedCommunity?.myCommunityData?.isActivated == false) {
            SignInState.NotActivatedState
        } else {
            SignInState.LoggedInState(userInfo)
        }
    }
    private fun onGoogleCredentialReceived(idToken: String) {
        saveEncryptedToken(idToken)
        processJwt(idToken)
// @todo Extraer información del idToken
        viewModelScope.launch {
            _signInState.value = SignInState.LoggingInState
            val result: Result<UserInfoModel> = withContext(Dispatchers.IO) {
                runCatching {
                    getUserInfo()
                }.onSuccess {
//                    userInfoViewModel.setUserInfo(it)
                    if (userSession.isFullLoggedIn())
                        userSession.logout()
                    userSession.initialize(it)
                    _signInState.value = whatSate(it)
                }.onFailure {
                    if (it is SocialUserNotFoundException) {
                        _signInState.value = SignInState.NotRegisteredState
                    } else {
                        _signInState.value =
                            SignInState.Error("Error while Signing In", kotlin.Exception(it))
                    }
                    Log.e("SignInViewModel", "Error while login", it)
                }
            }
//            userSession.userInfo = result

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
//    fun initViewModel(cManager: CredentialManager, userInfoViewModel: UserInfoViewModel) {
        //this.userInfoViewModel = userInfoViewModel
//        credentialManager = cManager
//    }
    fun initViewModel(cManager: CredentialManager) {
        credentialManager = cManager
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
            val jwt: JWT = org.fmm.communitymgmt.util.processJwt(idToken)
            if (idToken.isNotEmpty()) {
                // @TODO Es necesario eliminar tokens, etc. Se hará en userSession.logout().
                if (userSession.isLoggedIn()) {
                    userSession.logout()
                }
                userSession.initialize(
                    CredentialsData(jwt.subject, credential.idToken,credential.displayName,
                        credential.familyName, credential.givenName, credential
                            .profilePictureUri, credential.phoneNumber, "google", credential.id)
                )
                onGoogleCredentialReceived(idToken)
            } else {
                onSignInError(Exception("No ID Token received"))
            }
        }

    }
}