package org.fmm.communitymgmt.domainmodels.model

import io.fusionauth.jwt.JWTUtils
import io.fusionauth.jwt.domain.JWT

// Mapea con SocialUser de back
data class UserInfoModel (
    val socialUserInfo: SocialUserInfoModel,
    // Relationship with Person @IgnoredOnParcel
    val person: PersonModel? = null,
    val community: CommunityModel? = null,
    val allCommunities: List<CommunityModel>?=null,
    val dataJWT: String?=""
) {
    val jwt: JWT? = if (dataJWT != null && dataJWT.isNotEmpty())
        JWTUtils.decodePayload(dataJWT)
    else null
    /**
     * Not enrolled user
     */
    fun isFullEnrolled():Boolean {
        return (person != null && community != null && community?.isActivated ?: false)
    }

    /**
     * The userInfo record exists, but there isn't a community
     */
    fun isRegistering():Boolean {
        return (person != null && community == null)
    }
}