package org.fmm.communitymgmt.domainmodels.model

sealed class InvitationState (
    val id: Int,
    val name: String
) {
    data object Generated: InvitationState(0, "Generated")
    data object Processing: InvitationState(1, "Processing")
    data object Ended: InvitationState(2, "Ended")
}