package org.fmm.communitymgmt.ui.common.qr

import org.fmm.communitymgmt.domainmodels.model.FullInvitationModel
import org.fmm.communitymgmt.domainmodels.model.InvitationModel

sealed class QRReaderState {
    // Cámara apagada
    data object Stopped: QRReaderState()
    // Cámara encendida - No se usa nunca porque el modelo no lo activa, pero podría ser
    data object Scanning: QRReaderState()
    // Cuando ha leído, progress button visible e invoca al view model
    data object ProcessingQR: QRReaderState()
    // Cuando ha terminado la operación de procesar el QR. Progress button oculto
    data object QRProcessed:QRReaderState()
    data class Error(val errorMessage:String, val exception: Throwable): QRReaderState()
}