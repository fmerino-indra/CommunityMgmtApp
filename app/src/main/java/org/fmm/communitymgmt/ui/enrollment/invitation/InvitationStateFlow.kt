package org.fmm.communitymgmt.ui.enrollment.invitation

import org.fmm.communitymgmt.domainmodels.model.FullInvitationModel

sealed class InvitationStateFlow {
    data object Scanning: InvitationStateFlow()
    data object Loading: InvitationStateFlow()
    data class NotSelectedInvitation(val invitationList:List<FullInvitationModel>):
        InvitationStateFlow()
    data class SelectedInvitation(val invitation:FullInvitationModel): InvitationStateFlow()
    data class SignedInvitation(val invitation:FullInvitationModel): InvitationStateFlow()

    data class Error(val errorMessage:String, val exception: Throwable): InvitationStateFlow()
}