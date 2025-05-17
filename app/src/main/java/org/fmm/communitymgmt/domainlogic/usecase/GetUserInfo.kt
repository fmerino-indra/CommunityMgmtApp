package org.fmm.communitymgmt.domainlogic.usecase

import android.util.Log
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import io.fusionauth.jwt.JWTUtils
import org.fmm.communitymgmt.domainlogic.repositories.UserInfoRepository
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import org.fmm.communitymgmt.ui.security.util.EncryptedPrefsStorage
import javax.inject.Inject

class GetUserInfo @Inject constructor(private val userInfoRepository:UserInfoRepository,
                                      private val encryptedPrefsStorage: EncryptedPrefsStorage
) {
    suspend operator fun invoke(): UserInfoModel {
        try {
            return userInfoRepository.getUserInfo()
        } catch (re: RuntimeException) {
            // @todo Acceder a BBDD local room para ver si el user está ya enrolado y hay datos
            Log.d("GetUserInfo", "Unspected UserInfo $re")
            throw re
        }
/*
        val idToken = encryptedPrefsStorage.getString("auth_token")
        val jwt = JWTUtils.decodePayload(idToken)

        Log.d("SignInViewModel", "$jwt")
        return UserInfoModel(id = null,
            name = jwt.otherClaims["name"].toString(),
            email = jwt.otherClaims["email"]?.toString()+"",
            emailVerified =  jwt.otherClaims["emailVerified"]?.toString().toBoolean(),
            providerId = jwt.subject,
            provider = jwt.otherClaims["provider"].toString(),
            imageUrl = jwt.otherClaims["imageUrl"].toString()
        )

 */
    }
}