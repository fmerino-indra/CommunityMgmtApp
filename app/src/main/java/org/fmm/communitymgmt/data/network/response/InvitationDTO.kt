package org.fmm.communitymgmt.data.network.response

import kotlinx.serialization.Serializable
import org.fmm.communitymgmt.domainmodels.model.InvitationModel
import org.fmm.communitymgmt.domainmodels.model.InvitationState

@Serializable
data class InvitationDTO(
    val id: Int?=null,
    val name: String="",
    val communityId:Int,
    val personId:Int?=null,
    val signature:String?="",
    val nbf: Long?=null,
    val exp: Long?=null,
    val iat: Long?=null,
    val kpub: String?=null,
    val forMarriage: Boolean=false,
    val state: String,
    val invitationTypeId: Int
) {
    fun toDomain(): InvitationModel = InvitationModel(id, name, communityId, personId, signature,
        nbf, exp,
        iat, kpub, forMarriage,
        state = when(state) {
            "G" -> InvitationState.Generated
            "P" -> InvitationState.Processing
            "F" -> InvitationState.Finished
            else -> InvitationState.Generated
        }, invitationTypeId
    )

    companion object {
        fun fromDomain(invitation: InvitationModel) =
            with(invitation) {
                InvitationDTO(id, name, communityId, personId, signature, nbf, exp, iat, kpub,
                    forMarriage, state.id, invitationType)
            }
    }
}
