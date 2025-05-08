package org.fmm.communitymgmt.domainmodels.model

data class InvitationModel(
    val id: Int?=null,
    val name: String="",
    val communityId:Int,
    val signature:String="",
    val nbf: Long?=null,
    val exp: Long?=null,
    val state: InvitationState
) {
}