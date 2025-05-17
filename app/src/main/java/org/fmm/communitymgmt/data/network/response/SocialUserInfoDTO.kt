package org.fmm.communitymgmt.data.network.response

import kotlinx.serialization.Serializable
import org.fmm.communitymgmt.domainmodels.model.SocialUserInfoModel
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel

@Serializable
data class SocialUserInfoDTO (
    // Unique internal Id
    val id:Int?=null,
    val name: String,
    val email: String,
    val emailVerified: Boolean,
    val providerId: String?,
    val provider: String,
    val imageUrl: String,
    // Relationship with Person
) {
    fun toDomain():SocialUserInfoModel = SocialUserInfoModel(
        id, name, email, emailVerified, providerId, provider, imageUrl
    )


    companion object {
        fun fromDomain(userInfo: SocialUserInfoModel) =
            with(userInfo) {

                SocialUserInfoDTO(id,name,email,emailVerified,
                    providerId,provider,imageUrl)
            }
    }

}
