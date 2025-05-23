package org.fmm.communitymgmt.ui.enrollment.brothers

import org.fmm.communitymgmt.domainmodels.model.InvitationModel

sealed class BrothersEnrollmentState {
    data object Loading: BrothersEnrollmentState()
    data class Error(val errorMessage:String, val exception: Throwable): BrothersEnrollmentState()
    data class Success(val invitations:List<InvitationModel>): BrothersEnrollmentState()
}