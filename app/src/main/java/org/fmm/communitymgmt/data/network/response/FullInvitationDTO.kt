package org.fmm.communitymgmt.data.network.response

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import org.fmm.communitymgmt.domainmodels.model.FullInvitationModel
import org.fmm.communitymgmt.domainmodels.model.InvitationModel
import org.fmm.communitymgmt.domainmodels.model.InvitationState

@Serializable
data class FullInvitationDTO(
    val id: Int?=null,
    val name: String="",
    val iat: Long?=null,
    val nbf: Long?=null,
    val exp: Long?=null,
    val forMarriage: Boolean=false,
    val state: String,
    val invitationType: Int,
    val communityId: Int,
    val communityFullName: String,
    val communityCity: String,
    val communityCountry: String,
    val personId: Int,
    val personFullName: String,
    val personPublicKey: String?=null,
    val personSignature: String?="",
    val responsibleId: Int,
    val responsibleFullName: String,
    val responsibleSignature:String?="",
    val responsiblePublicKey: String?=null
) {
    fun toDomain(): FullInvitationModel = FullInvitationModel(id, name,iat,nbf, exp,forMarriage,
        state = when(state) {
            "G" -> InvitationState.Generated
            "P" -> InvitationState.Processing
            "F" -> InvitationState.Finished
            else -> InvitationState.Generated
        },
        invitationType,
        communityId,
        communityFullName,
        communityCity,
        communityCountry,
        personId,
        personFullName,
        personPublicKey,
        personSignature,
        responsibleId,
        responsibleFullName,
        responsiblePublicKey,
        responsibleSignature
    )

    companion object {
        fun fromDomain(invitation: FullInvitationModel) =
            with(invitation) {
                FullInvitationDTO(id, name,iat,nbf,exp, forMarriage,
                    state.id,invitationType,
                    communityId,
                    communityFullName,
                    communityCity,
                    communityCountry,
                    personId,
                    personFullName,
                    personPublicKey,
                    personSignature,
                    responsibleId,
                    responsibleFullName,
                    responsiblePublicKey,
                    responsibleSignature
                )
            }
    }
}
