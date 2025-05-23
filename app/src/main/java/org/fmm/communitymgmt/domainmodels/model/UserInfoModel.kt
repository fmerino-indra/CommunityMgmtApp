package org.fmm.communitymgmt.domainmodels.model

import io.fusionauth.jwt.JWTUtils
import io.fusionauth.jwt.domain.JWT

// Mapea con SocialUser de back
data class UserInfoModel (
    val socialUserInfo: SocialUserInfoModel,
    // Relationship with Person @IgnoredOnParcel
    val person: PersonModel,
    private var _selectedCommunity: CommunityInfoModel? = null,
    val myCommunities: List<CommunityInfoModel>,
    val dataJWT: String?=""
) {
    val selectedCommunity get() = _selectedCommunity

    val jwt: JWT? = if (!dataJWT.isNullOrEmpty())
        JWTUtils.decodePayload(dataJWT)
    else null
    /**
     * Not enrolled user
     */
    fun isFullEnrolled():Boolean {
        //return (person != null && community != null && community?.isActivated ?: false)
        return (myCommunities.isNotEmpty())
    }

    /**
     * The userInfo record exists, but there isn't a community
     */
    fun isRegistering():Boolean {
        return myCommunities.isEmpty()
    }

    /**
     * Only if is Responsible
     */
    fun isResponsible(): Boolean {
        if (isFullEnrolled() && selectedCommunity != null && myCommunities.isNotEmpty()) {
            return selectedCommunity!!.myCharges?.contains(TChargeModel.Responsible) ?: false
        }
        return false
    }

    /**
     * Can manage this community
     * @TODO Complete with all non functional charges (delegated)
     */
    fun canManage(): Boolean {
        if (isFullEnrolled() && selectedCommunity != null) {
            return selectedCommunity?.myCharges?.contains(TChargeModel.Responsible) ?: false||
                    selectedCommunity?.myCharges?.contains(TChargeModel.ResponsibleSpouse) ?: false
        }
        return false
    }

    fun setSelectedCommunity (selected: CommunityInfoModel) {
        _selectedCommunity = selected
    }
}