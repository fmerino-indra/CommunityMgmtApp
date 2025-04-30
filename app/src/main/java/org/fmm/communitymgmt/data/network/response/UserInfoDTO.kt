package org.fmm.communitymgmt.data.network.response

import kotlinx.serialization.Serializable
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel

@Serializable
data class UserInfoDTO (
    // Unique internal Id
    val id:Int,
    // Unique IdP functional Id
    //val upn:String,
    // Unique IdP internal Id (IdPUserId)
    val userId:String,
    val name: String,
    val email: String,
    val emailVerified: Boolean,
    val providerId: Int,
    val provider: String,
    val imageUrl: String?,
    // Relationship with Person
    val person: PersonDTO?=null
) {
    fun toDomain():UserInfoModel = UserInfoModel(
        id, userId, name, email, emailVerified, providerId, provider, imageUrl, person?.toDomain()
    )


    companion object {
        fun fromDomain(userInfo: UserInfoModel) {
            with(userInfo) {
                UserInfoDTO(id,userId,name,email,emailVerified,
                    providerId,provider,imageUrl)
            }
        }
    }

}
