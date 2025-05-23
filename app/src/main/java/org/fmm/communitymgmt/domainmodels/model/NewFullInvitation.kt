package org.fmm.communitymgmt.domainmodels.model

data class NewFullInvitation(
    val id: Int?=null,
    val name: String="",
    val iat: Long?=null,
    val nbf: Long?=null,
    val exp: Long?=null,
    val state: InvitationState,
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
}
