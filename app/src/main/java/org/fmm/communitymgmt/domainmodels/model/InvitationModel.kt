package org.fmm.communitymgmt.domainmodels.model

data class InvitationModel(
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
    val state: InvitationState,
    val invitationType: Int=1
) {
}