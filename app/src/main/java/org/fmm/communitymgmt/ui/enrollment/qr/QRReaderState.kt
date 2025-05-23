package org.fmm.communitymgmt.ui.enrollment.qr

import org.fmm.communitymgmt.domainmodels.model.FullInvitationModel
import org.fmm.communitymgmt.domainmodels.model.InvitationModel

sealed class QRReaderState {
    data object Scanning: QRReaderState()
    data object Loading: QRReaderState()
    data class Error(val errorMessage:String, val exception: Throwable): QRReaderState()
    data class Success(val invitation:FullInvitationModel): QRReaderState()
}