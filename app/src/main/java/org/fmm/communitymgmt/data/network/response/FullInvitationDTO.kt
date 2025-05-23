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
    val community:CommunityDTO,
    val person:PersonDTO,
    val invitationRelationship: @Polymorphic AbstractRelationshipDTO,
    val responsibleSignature:String?="",
    val responsiblePublicKey: String?=null,
    val personSignature: String?="",
    val personPublicKey: String?=null
) {
    fun toDomain(): FullInvitationModel = FullInvitationModel(id, name,iat,nbf, exp,forMarriage,
        state = when(state) {
            "G" -> InvitationState.Generated
            "P" -> InvitationState.Processing
            "F" -> InvitationState.Finished
            else -> InvitationState.Generated
        },
        invitationType, community.toDomain(), person.toDomain(),
        invitationRelationship = invitationRelationship.toDomain(),
        responsibleSignature,
        responsiblePublicKey,
        personSignature,
        personPublicKey
    )

    companion object {
        fun fromDomain(invitation: FullInvitationModel) =
            with(invitation) {
                FullInvitationDTO(id, name,iat,nbf,exp, forMarriage,
                    state.id,invitationType,
                    CommunityDTO.fromDomain(community),
                    PersonDTO.fromDomain(person),
                    AbstractRelationshipDTO.fromDomain(invitationRelationship),
                    responsibleSignature, responsiblePublicKey,
                    personSignature, personPublicKey)
            }
    }
}
