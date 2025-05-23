package org.fmm.communitymgmt.data.network.response

import kotlinx.serialization.Serializable
import org.fmm.communitymgmt.domainmodels.model.CommunityInfoModel
import java.util.stream.Collectors

@Serializable
data class CommunityInfoDTO(
    val myCommunityData: CommunityDTO,
    val myCharges:List<TChargeDTO>?
) {
    fun toDomain(): CommunityInfoModel = CommunityInfoModel(
        myCommunityData = myCommunityData.toDomain(),
        myCharges = myCharges?.stream()?.map {
            it.toDomain()
        }?.collect(Collectors.toList())
    )

    companion object {
        fun fromDomain(communityInfoModel: CommunityInfoModel) =
            communityInfoModel.run {
                CommunityInfoDTO (
                    myCommunityData = CommunityDTO.fromDomain(myCommunityData),
                    myCharges = myCharges?.stream()
                        ?.map { TChargeDTO.fromDomain(it)}
                        ?.collect(Collectors.toList())
                )


            }
    }
}