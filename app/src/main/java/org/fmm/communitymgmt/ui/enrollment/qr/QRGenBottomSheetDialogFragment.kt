package org.fmm.communitymgmt.ui.enrollment.qr

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.fmm.communitymgmt.databinding.FragmentQRGenBinding

class QRGenBottomSheetDialogFragment (private val qrBitmap: Bitmap): BottomSheetDialogFragment() {
    private var _binding: FragmentQRGenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQRGenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.qrImageView.setImageBitmap(qrBitmap)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}