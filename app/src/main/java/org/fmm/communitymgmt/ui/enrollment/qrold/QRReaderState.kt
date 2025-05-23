package org.fmm.communitymgmt.ui.enrollment.qrold

import org.fmm.communitymgmt.domainmodels.model.FullInvitationModel

sealed class QRReaderState {
    data object Scanning: QRReaderState()
    data object Loading: QRReaderState()
    data class Error(val errorMessage:String, val exception: Throwable): QRReaderState()
    data class Success(val invitation:FullInvitationModel): QRReaderState()
}