package org.fmm.communitymgmt.data.network.response

import kotlinx.serialization.Serializable
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel

@Serializable
data class UserInfoDTO (
    // Unique internal Id
    val id:Int?=null,
    val name: String,
    val email: String,
    val emailVerified: Boolean,
    val providerId: String?,
    val provider: String,
    val imageUrl: String,
    // Relationship with Person
    val person: PersonDTO?=null
) {
    fun toDomain():UserInfoModel = UserInfoModel(
        id, name, email, emailVerified, providerId, provider, imageUrl, person?.toDomain()
    )


    companion object {
        fun fromDomain(userInfo: UserInfoModel) =
            with(userInfo) {

                UserInfoDTO(id,name,email,emailVerified,
                    providerId,provider,imageUrl, person?.let { PersonDTO.fromDomain(it) })
            }
    }

}
