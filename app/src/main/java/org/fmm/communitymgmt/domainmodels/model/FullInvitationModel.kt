package org.fmm.communitymgmt.domainmodels.model

data class FullInvitationModel(
    val id: Int?=null,
    val name: String="",
    val iat: Long?=null,
    val nbf: Long?=null,
    val exp: Long?=null,
    val forMarriage: Boolean=false,
    val state: InvitationState,
    val invitationType: Int,
    val community:CommunityModel,
    val person:PersonModel,
    val invitationRelationship: AbstractRelationship,
    val responsibleSignature:String?="",
    val responsiblePublicKey: String?=null,
    val personSignature: String?="",
    val personPublicKey: String?=null
) {
}
