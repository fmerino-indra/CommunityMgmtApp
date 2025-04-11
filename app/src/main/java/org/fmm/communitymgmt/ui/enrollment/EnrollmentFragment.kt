package org.fmm.communitymgmt.ui.enrollment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.databinding.FragmentEnrollmentBinding

/**
 * A simple [Fragment] subclass.
 * Use the [EnrollmentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class EnrollmentFragment : Fragment() {

    private var _binding: FragmentEnrollmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnrollmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}