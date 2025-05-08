package org.fmm.communitymgmt.data.network.response

import kotlinx.serialization.Serializable
import org.fmm.communitymgmt.domainmodels.model.CommunityModel

@Serializable
data class CommunityDTO(
    val id: Int?,
    val communityNumber:String,
    val parish: String,
    val parishAddress: String,
    val parishAddressNumber: String,
    val parishAddressPostalCode: String,
    val parishAddressCity: String,
    val isActivated: Boolean
) {
    fun toDomain(): CommunityModel = CommunityModel(
        id, communityNumber, parish, parishAddress, parishAddressNumber, parishAddressPostalCode,
        parishAddressCity, isActivated
    )

    companion object {
        fun fromDomain(community: CommunityModel) =
            with(community) {
                CommunityDTO(id, communityNumber, parish, parishAddress, parishAddressNumber,
                    parishAddressPostalCode, parishAddressCity, isActivated)
            }
    }
}
