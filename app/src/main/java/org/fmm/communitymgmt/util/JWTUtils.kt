package org.fmm.communitymgmt.util

import android.util.Log
import io.fusionauth.jwt.JWTUtils
import io.fusionauth.jwt.domain.JWT

fun processJwt(idToken: String): JWT {
    val jwt = JWTUtils.decodePayload(idToken)
    Log.d("SignInViewModel", "$jwt")
    return jwt
}

