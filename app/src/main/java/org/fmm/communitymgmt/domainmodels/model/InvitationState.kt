package org.fmm.communitymgmt.domainmodels.model

sealed class InvitationState (
    val id: String,
    val name: String
) {
    data object Generated: InvitationState("G", "Generated")
    data object Processing: InvitationState("P", "Processing")
    data object Finished: InvitationState("F", "Finished")
}