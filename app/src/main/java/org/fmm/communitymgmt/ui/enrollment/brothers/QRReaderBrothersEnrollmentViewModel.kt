package org.fmm.communitymgmt.ui.enrollment.brothers

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.fmm.communitymgmt.ui.common.qr.QRReaderState
import org.fmm.communitymgmt.ui.security.model.UserSession
import javax.inject.Inject

@HiltViewModel
class QRReaderBrothersEnrollmentViewModel @Inject constructor(
    private val userSession: UserSession,
    private val useCases: Map<String, @JvmSuppressWildcards ScanUseCase>
) : ViewModel() {
    private var _qrReaderState: MutableStateFlow<QRReaderState>
            = MutableStateFlow (QRReaderState.Stopped)
    val qrReaderState: StateFlow<QRReaderState> get() = _qrReaderState

    private lateinit var useCase:ScanUseCase

    fun setUseCaseKey(key:String) {
        useCase = useCases[key] ?: throw IllegalArgumentException("No useCase registered for key:" +
                " $key")
    }
    fun initData() {
        Log.d("QRReaderBrothersEnrollmentViewModel", userSession.credential.email)
    }
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


    fun processQR(uri: String) {
        Log.d("QRReaderBrothersEnrollmentViewModel", "Se ha leído: $uri")
    }
}