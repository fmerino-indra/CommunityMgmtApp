package org.fmm.communitymgmt.ui.common.qr

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
@Deprecated("No ha funcionado esta técnica")
abstract class BaseQRReaderViewModel: ViewModel() {
    private var _qrReaderState: MutableStateFlow<QRReaderState>
            = MutableStateFlow (QRReaderState.Stopped)
    val qrReaderState:StateFlow<QRReaderState> get() = _qrReaderState

    abstract fun initData()

    fun onQRRead(uri:String) {
        _qrReaderState.value = QRReaderState.ProcessingQR
        try {
            processQR(uri)
            _qrReaderState.value = QRReaderState.QRProcessed
        } catch (e: Exception) {
            Log.e("QRReaderViewModel", "An error has occurred", e)
            _qrReaderState.value = QRReaderState.Error(e.message ?: ("An error " +
                        "has occurred"), e)
        }
    }

    abstract fun processQR(uri:String)

}