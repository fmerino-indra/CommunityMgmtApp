package org.fmm.communitymgmt.data.network.response

import kotlinx.serialization.Serializable
import org.fmm.communitymgmt.domainmodels.model.InvitationModel
import org.fmm.communitymgmt.domainmodels.model.InvitationState

@Serializable
data class InvitationDTO(
    val id: Int?=null,
    val name: String="",
    val communityId:Int,
    val signature:String="",
    val nbf: Long?=null,
    val exp: Long?=null,
    val state: Int
) {
    fun toDomain(): InvitationModel = InvitationModel(id, name, communityId, signature, nbf, exp,
        state = when(state) {
            0 -> InvitationState.Generated
            1 -> InvitationState.Processing
            2 -> InvitationState.Ended
            else -> InvitationState.Generated
        }
    )

    companion object {
        fun fromDomain(invitation: InvitationModel) =
            with(invitation) {
                InvitationDTO(id, name, communityId, signature, nbf, exp, state.id)
            }
    }
}
