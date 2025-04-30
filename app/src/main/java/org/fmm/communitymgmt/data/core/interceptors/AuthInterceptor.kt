package org.fmm.communitymgmt.data.core.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import org.fmm.communitymgmt.ui.security.util.EncryptedPrefsStorage
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val encryptedPrefsStorage: EncryptedPrefsStorage)
    :Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", encryptedPrefsStorage.getString("auth_token")?:"")
            .build()
        return chain.proceed(request)
    }
}