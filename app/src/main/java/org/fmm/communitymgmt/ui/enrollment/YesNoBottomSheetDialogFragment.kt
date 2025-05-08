package org.fmm.communitymgmt.ui.enrollment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.fmm.communitymgmt.databinding.FragmentYesNoBottomSheetDialogBinding

class YesNoBottomSheetDialogFragment (
    private val question: String,
    private val onSelected: (isYes:Boolean) -> Unit
    ): BottomSheetDialogFragment() {
    private var _binding:FragmentYesNoBottomSheetDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentYesNoBottomSheetDialogBinding.inflate(layoutInflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding.titulo.text = question
        initListeners()
    }

    private fun initListeners() {
        binding.btnYes.setOnClickListener {
            onSelected(true)
            dismiss()
        }

        binding.btnNo.setOnClickListener {
            onSelected(false)
            dismiss()
        }
    }
}