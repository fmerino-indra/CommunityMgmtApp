package org.fmm.communitymgmt.ui.enrollment.qr

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.databinding.FragmentQRReaderBinding

class QRReaderFragment : Fragment() {
    private var _binding: FragmentQRReaderBinding? = null
    private val binding get() = _binding!!

    private lateinit var barcodeCallback: BarcodeCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQRReaderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        barcodeCallback = object:BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                result?.text.let {
                    Toast.makeText(requireContext(), "QR: $it", Toast.LENGTH_LONG).show()
                    stopScanner()
                }
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

    private fun stopScanner() {
        binding.barcodeScannerView.apply {
            pause()
            visibility = View.GONE
        }
    }

    private fun startScanner() {
        binding.barcodeScannerView.apply {
            visibility = View.VISIBLE
            decodeContinuous(barcodeCallback)
            resume()
        }
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
