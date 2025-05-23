package org.fmm.communitymgmt.data.network.response

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import org.fmm.communitymgmt.domainmodels.model.FullInvitationModel
import org.fmm.communitymgmt.domainmodels.model.InvitationModel
import org.fmm.communitymgmt.domainmodels.model.InvitationState
import org.fmm.communitymgmt.domainmodels.model.NewFullInvitation

@Serializable
data class NewFullInvitationDTO(
    val id: Int?=null,
    val name: String="",
    val iat: Long?=null,
    val nbf: Long?=null,
    val exp: Long?=null,
    val state: String,
    val forMarriage: Boolean=false,
    val invitationType: Int,

    val communityId:Int,
    val communityFullName: String,

    val personId:Int,
    val personFullName: String,
    val personSignature: String?="",
    val personPublicKey: String?=null,

    val responsibleId:Int,
    val responsibleFullName: String,
    val responsibleSignature:String?="",
    val responsiblePublicKey: String?=null
) {
    fun toDomain(): NewFullInvitation = NewFullInvitation(id, name,iat,nbf, exp,
        when(state) {
            "G" -> InvitationState.Generated
            "P" -> InvitationState.Processing
            "F" -> InvitationState.Finished
            else -> InvitationState.Generated
        }, forMarriage, invitationType,
        communityId, communityFullName,
        personId, personFullName, personSignature, personPublicKey,
        responsibleId, responsibleFullName, responsibleSignature
    )

    companion object {
        fun fromDomain(invitation: NewFullInvitation) =
            with(invitation) {
                NewFullInvitationDTO(id, name,iat,nbf,exp,
                    state.id,forMarriage,invitationType,
                    communityId, communityFullName,
                    personId, personFullName, personSignature, personPublicKey,
                    responsibleId, responsibleFullName, responsibleSignature, responsiblePublicKey
                )
            }
    }
}
