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
    val responsiblePublicKey: String?=null,
) {
}
