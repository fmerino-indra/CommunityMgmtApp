package org.fmm.communitymgmt.ui.enrollment.brothers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import org.fmm.communitymgmt.ui.common.qr.BaseQRReaderFragment

//@AndroidEntryPoint
class QRReaderBrothersEnrollmentFragment:
    BaseQRReaderFragment<QRReaderBrothersEnrollmentViewModel>() {
        override lateinit var qrReaderViewModel: QRReaderBrothersEnrollmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val aux = super.onCreateView(inflater, container, savedInstanceState)
        qrReaderViewModel = ViewModelProvider(this)[QRReaderBrothersEnrollmentViewModel::class.java]
        return aux
    }
}