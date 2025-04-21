package org.fmm.communitymgmt.ui.security.util

import android.app.Activity
import android.content.Context
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.SecureCredentialsManager
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import dagger.hilt.android.qualifiers.ApplicationContext
import org.fmm.communitymgmt.R
import javax.inject.Inject

class Auth0Manager @Inject constructor (
    @ApplicationContext context: Context,
    private val encryptedPrefsStorage: EncryptedPrefsStorage,
    secureConfigManager: SecureConfigManager
) {
    private val authTokenKey = context.getString(R.string.auth_token_key)
    private val account = Auth0.getInstance(secureConfigManager.getClientId()!!,
        secureConfigManager.getDomain()!!)
/*
    private val apiClient: AuthenticationAPIClient by lazy {
        AuthenticationAPIClient(account)
    }
 */

/*
Hay que elegir entre uno de los dos credentialManager:
 */
//    private val credentialsManager: CredentialsManager by lazy {
//        CredentialsManager(apiClient, SharedPreferencesStorage(context))
//    }

    private val secureCredentialsManager: SecureCredentialsManager=SecureCredentialsManager(context, account, encryptedPrefsStorage)

    fun saveAuthToken(token:String) {
        encryptedPrefsStorage.saveString(authTokenKey, token)
    }

    fun getAuthToken():String? {
        return encryptedPrefsStorage.getString(authTokenKey)
    }

    fun clearAuthToken() {
        encryptedPrefsStorage.deleteValue(authTokenKey)
    }

    fun isAuthenticated():Boolean = secureCredentialsManager.hasValidCredentials()

//    fun getCredentials(callback: Callback<Credentials, CredentialsManagerException>) {
//        secureCredentialsManager.getCredentials(callback)
//    }

    fun login(activity: Activity, callback: Callback<Credentials, AuthenticationException>) {
        WebAuthProvider.login(account)
            .withScheme("")
            .withConnection("")
            .withScope("openid profile email offline_access")
            .withAudience("")
            .start(activity, object: Callback<Credentials, AuthenticationException> {
                override fun onFailure(error: AuthenticationException) {
                    callback.onFailure(error)
                }

                override fun onSuccess(result: Credentials) {
                    secureCredentialsManager.saveCredentials(result)
                    callback.onSuccess(result)
                }

            })
        fun logout(activity: Activity, callback: Callback<Void?, AuthenticationException>) {
            WebAuthProvider.logout(account)
                .withScheme("")
                .start(activity, object:Callback<Void?, AuthenticationException>{
                    override fun onFailure(error: AuthenticationException) {
                        callback.onFailure(error)
                    }

                    override fun onSuccess(result: Void?) {
                        secureCredentialsManager.clearCredentials()
                        callback.onSuccess(result)
                    }
                })
        }

        fun clear() {
            secureCredentialsManager.clearCredentials()
        }
    }
}