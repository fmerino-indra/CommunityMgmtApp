package org.fmm.communitymgmt.ui.enrollment.qr

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import kotlinx.coroutines.launch
import org.fmm.communitymgmt.databinding.FragmentQRReaderBinding
import org.fmm.communitymgmt.ui.enrollment.brothers.BrothersEnrollmentState
import org.fmm.communitymgmt.ui.enrollment.brothers.BrothersEnrollmentViewModel
import org.fmm.communitymgmt.util.playBeep
import org.fmm.communitymgmt.util.vibrate

class QRReaderFragment : Fragment() {
    private var _binding: FragmentQRReaderBinding? = null
    private val binding get() = _binding!!
    private val qrReaderViewModel by activityViewModels<QRReaderViewModel> ()

    private lateinit var barcodeCallback: BarcodeCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQRReaderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUIState()
        barcodeCallback = object:BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                if (result?.text != null && result.text.isNotEmpty())
                    processInvitation(result.text)
            }
        }

        binding.btnStartScan.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager
                    .PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    100
                )
            } else {
                startScanner()
            }
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                qrReaderViewModel.state.collect {
                    when(it) {
                        is QRReaderState.Error -> errorState(it)
                        QRReaderState.Loading -> loadingState()
                        is QRReaderState.Success -> successState(it)
                        QRReaderState.Scanning -> scanningState()
                    }
                }
            }
        }
    }

    private fun scanningState() {
        binding.progressBar.isVisible = false
        binding.btnStartScan.isVisible = true
        binding.btnStopScan.isVisible = false
    }

    private fun errorState(qrReaderState: QRReaderState.Error) {
        binding.progressBar.isVisible = false
        binding.btnStartScan.isVisible = true
        binding.btnStopScan.isVisible = false
        Log.e("[BrothersEnrollmentFragment]", qrReaderState.errorMessage,
            qrReaderState.exception)
        message(qrReaderState.errorMessage)
    }

    private fun loadingState() {
        binding.progressBar.isVisible = true
        binding.btnStartScan.isVisible = false
        binding.btnStopScan.isVisible = false
    }

    private fun successState(qrReaderState: QRReaderState.Success) {
        binding.progressBar.isVisible = false
        binding.btnStartScan.isVisible = false
        binding.btnStopScan.isVisible = false
        //@TODO Do something to process o if the
    }


    /**
     * @todo Procesar la invitación
     */
    private fun processInvitation(uri: String) {
        this.playBeep()
        this.vibrate()
        stopScanner()
        Toast.makeText(requireContext(), "QR: $uri", Toast.LENGTH_LONG).show()
        qrReaderViewModel.onInvitationRead(uri)

    }
    private fun stopScanner() {
        binding.btnStopScan.isVisible=false
        binding.btnStartScan.isVisible=true
        binding.barcodeScannerView.apply {
            pause()
            visibility = View.GONE
        }
    }

    private fun startScanner() {
        binding.btnStartScan.isVisible=false
        binding.btnStopScan.isVisible=true
        binding.barcodeScannerView.apply {
            visibility = View.VISIBLE
            decodeContinuous(barcodeCallback)
            resume()
        }
    }
    private fun message(message: String) {
        Toast.makeText(
            requireContext(), message, Toast.LENGTH_LONG
        ).show()

    }

    override fun onPause() {
        super.onPause()
        binding.barcodeScannerView.pause()
    }

    override fun onResume() {
        super.onResume()
        if(binding.barcodeScannerView.visibility == View.VISIBLE) {
            binding.barcodeScannerView.resume()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanner()
            } else {
                Toast.makeText(requireContext(), "Permiso de cámara necesario", Toast.LENGTH_LONG)
            }
        }

    }
    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }
}
