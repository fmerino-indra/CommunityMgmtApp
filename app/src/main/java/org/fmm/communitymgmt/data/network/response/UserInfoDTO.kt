package org.fmm.communitymgmt.data.network.response

import kotlinx.serialization.Serializable
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import java.util.stream.Collectors

@Serializable
data class UserInfoDTO (
    val socialUserInfo: SocialUserInfoDTO,
    // Relationship with Person
    val person: PersonDTO,
    val selectedCommunity: CommunityInfoDTO?=null,
    val myCommunities: List<CommunityInfoDTO>,
    val dataJWT: String?
) {
    fun toDomain():UserInfoModel = UserInfoModel(
        socialUserInfo.toDomain(),
        person.toDomain(),
        selectedCommunity?.toDomain(),
        myCommunities.stream().map {
            it.toDomain()
        }.collect(Collectors.toList()),
        dataJWT
    )


    companion object {
        fun fromDomain(userInfo: UserInfoModel) =
            with(userInfo) {

                UserInfoDTO(
                    socialUserInfo.let { SocialUserInfoDTO.fromDomain(it) },
                    person.let { PersonDTO.fromDomain(it)},
                    selectedCommunity?.let { CommunityInfoDTO.fromDomain(it) },
                    myCommunities.stream()
                        .map { com -> CommunityInfoDTO.fromDomain(com) }
                        .collect(Collectors.toList()),
                    dataJWT)
            }
    }
}
