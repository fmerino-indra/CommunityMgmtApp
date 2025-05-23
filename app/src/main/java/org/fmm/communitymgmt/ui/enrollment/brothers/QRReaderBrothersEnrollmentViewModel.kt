package org.fmm.communitymgmt.ui.enrollment.brothers

import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import org.fmm.communitymgmt.ui.common.qr.BaseQRReaderViewModel
import javax.inject.Inject

//@HiltViewModel
class QRReaderBrothersEnrollmentViewModel @Inject constructor() : BaseQRReaderViewModel() {
    override fun initData() {
    }

    override fun processQR(uri: String) {
        Log.d("QRReaderBrothersEnrollmentViewModel", "Se ha leído: $uri")
    }
}