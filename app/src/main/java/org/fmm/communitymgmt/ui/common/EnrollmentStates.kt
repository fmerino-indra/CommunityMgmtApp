package org.fmm.communitymgmt.ui.common

sealed class EnrollmentStates (
    val id: Int,
    val name: String
) {
    object NOTENROLLED:EnrollmentStates(0,"Not enrolled")
    object NOTCOMMUNITY:EnrollmentStates(1, "Not community")
    object FULLENROLLED:EnrollmentStates(2,"Full enrolled")
    object ACTIVATED:EnrollmentStates(3, "Activated")

}