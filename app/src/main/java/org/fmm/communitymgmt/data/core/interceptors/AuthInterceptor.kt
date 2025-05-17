package org.fmm.communitymgmt.data.core.interceptors

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import org.fmm.communitymgmt.ui.security.model.UserSession
import org.fmm.communitymgmt.ui.security.util.EncryptedPrefsStorage
import javax.inject.Inject
import kotlin.math.log

class AuthInterceptor @Inject constructor(
    private val encryptedPrefsStorage: EncryptedPrefsStorage,
    private val userSession: UserSession)
    :Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = encryptedPrefsStorage.getString("auth_token")
        Log.d("AuthInterceptor", "El Data JWT: ${userSession.userInfo?.dataJWT ?: "" }")
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")

            .addHeader("X-data-jwt", userSession.userInfo?.dataJWT ?: "")
            .build()
        return chain.proceed(request)
    }
}