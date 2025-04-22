package org.fmm.communitymgmt.ui.security.google

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import org.fmm.communitymgmt.R

class GoogleAuthManager(
    private val context: Context,
    private val callback: AuthCallback?
) {
    companion object {
        const val REQUEST_CODE = 100
        const val REDIRECT_URI = "com.googleusercontent.apps.828614483216-ql47ddnq773mt6ge92ak0f1idkmlsb6d:/oauth2redirect"
//        const val CLIENT_ID = "828614483216-ql47ddnq773mt6ge92ak0f1idkmlsb6d.apps" +
//                ".googleusercontent.com"
        const val CLIENT_ID = "828614483216-4qvhvjf40tokokb7vjjgv1m1589i9ds7.apps.googleusercontent.com"

        /*
                              "828614483216-ql47ddnq773mt6ge92ak0f1idkmlsb6d.apps.googleusercontent.com"
                              "828614483216-4qvhvjf40tokokb7vjjgv1m1589i9ds7.apps.googleusercontent.com"

         */
    }
    private var authService: AuthorizationService = AuthorizationService(context)

    fun getAuthIntent(): Intent {
        val serviceConfig = AuthorizationServiceConfiguration(
            Uri.parse(context.getString(R.string.google_auth)),
            Uri.parse(context.getString(R.string.google_token))
        )
        AuthorizationServiceConfiguration.fetchFromIssuer(Uri.parse
            ("https://accounts.google.com")) { config, ex ->
                if (config != null) {
                    Log.d("GoogleAuthManager", "El config: ${config.toJsonString()}")
                } else {
                    Log.e("GoogleAuthManager", "Algo fue mal al conseguir el config", ex)

                }
        }

        Log.d("GoogleAuthManager", "redirect_uri: ${REDIRECT_URI}")

        val authRequest = AuthorizationRequest.Builder(
            serviceConfig,
            CLIENT_ID,
            ResponseTypeValues.CODE,
            Uri.parse(REDIRECT_URI)
        )
            .setScopes("openid", "profile", "email")
            .build()
        return authService.getAuthorizationRequestIntent(authRequest)
    }

    fun handleAuthResult(
        data: Intent?,
        onSuccess: (idToken: String?) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val response = AuthorizationResponse.fromIntent(data!!)
        val exception = AuthorizationException.fromIntent(data)

        if (response != null) {
            val request = response.createTokenExchangeRequest()
            authService.performTokenRequest(request) {
                tokenResponse, ex ->
                if (tokenResponse != null) {
                    onSuccess(tokenResponse.idToken)
                } else {
                    onError(ex ?: Exception ("Error desconocido"))
                }
            }
        } else {
            onError(exception ?: Exception("Error en el login"))
        }
    }

    fun dispose() {
        authService.dispose()
    }
}