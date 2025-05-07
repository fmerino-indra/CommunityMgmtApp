package org.fmm.communitymgmt.ui.enrollment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.databinding.FragmentEnrollmentBinding

/**
 * A simple [Fragment] subclass.
 * Use the [EnrollmentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class EnrollmentFragment : Fragment(), ResponsibleDialog.NoticeDialogListener {

    private var _binding: FragmentEnrollmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnrollmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun askResponsible() {
        val newFragment = ResponsibleDialog(this)
        newFragment.show(parentFragmentManager, "isResponsible")
    }
    override fun onDialogPositiveClick(dialog: DialogFragment) {
        Log.d("dialogListener", "Ha pulsado Sí")
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        Log.d("dialogListener", "Ha pulsado No")
    }


}