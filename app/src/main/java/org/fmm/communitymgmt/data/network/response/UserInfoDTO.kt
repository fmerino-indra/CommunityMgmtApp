package org.fmm.communitymgmt.data.network.response

import kotlinx.serialization.Serializable
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import java.util.stream.Collectors

@Serializable
data class UserInfoDTO (
    val socialUserInfo: SocialUserInfoDTO,
    // Relationship with Person
    val person: PersonDTO?=null,
    val community: CommunityDTO?=null,
    val allCommunities: List<CommunityDTO>?=null,
    val dataJWT: String?
) {
    fun toDomain():UserInfoModel = UserInfoModel(
        socialUserInfo.toDomain(),
        person?.toDomain(),
        community?.toDomain(),
        allCommunities?.stream()?.map {
            it.toDomain()
        }?.collect(Collectors.toList()),
        dataJWT
    )


    companion object {
        fun fromDomain(userInfo: UserInfoModel) =
            with(userInfo) {

                UserInfoDTO(socialUserInfo.let { SocialUserInfoDTO.fromDomain(it) },
                    person?.let { PersonDTO.fromDomain(it)},
                    community?.let { CommunityDTO.fromDomain(it) },
                    allCommunities?.stream()?.map { com ->
                        CommunityDTO.fromDomain(com)
                    }?.collect(Collectors.toList()),
                    dataJWT)
            }
    }
}
