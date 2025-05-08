package org.fmm.communitymgmt.ui.enrollment.brothers

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.databinding.FragmentBrothersEnrollmentBinding
import org.fmm.communitymgmt.domainmodels.model.InvitationModel
import org.fmm.communitymgmt.ui.enrollment.brothers.recyclerview.InvitationListAdapter
import org.fmm.communitymgmt.ui.enrollment.qr.QRGenBottomSheetDialogFragment


@AndroidEntryPoint
class BrothersEnrollmentFragment : Fragment() {
    private val brothersEnrollmentViewModel by viewModels<BrothersEnrollmentViewModel> ()

    private var _binding: FragmentBrothersEnrollmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var invitationListAdapter: InvitationListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBrothersEnrollmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initUI()
    }

    private fun initData() {
        brothersEnrollmentViewModel.initData()
    }

    private fun initUI() {
        initAdapter()
        initUIState()
    }

    private fun initAdapter() {
        invitationListAdapter = InvitationListAdapter(onItemSelected = {
            generateQR(it)
        })

        binding.rvInvitationList.apply {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = invitationListAdapter
        }
        initRefreshList()
    }
    private fun initRefreshList() {
        binding.swipeRefreshLayout.setOnRefreshListener(OnRefreshListener {
            binding.swipeRefreshLayout.setRefreshing(false)
            brothersEnrollmentViewModel.initData()
        })

    }
    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                brothersEnrollmentViewModel.state.collect {
                    when(it) {
                        is BrothersEnrollmentState.Error -> errorState()
                        BrothersEnrollmentState.Loading -> loadingState()
                        is BrothersEnrollmentState.Success -> successState(it)
                    }
                }
            }
        }
    }

    private fun errorState() {
        binding.progressBar.isVisible = false
        Log.d("[FMMP]", "Se ha producido un error al recibir o pintar la información")
    }

    private fun loadingState() {
        binding.progressBar.isVisible = true
    }

    private fun successState(state: BrothersEnrollmentState.Success) {
        binding.progressBar.isVisible = false
        invitationListAdapter.updateInvitationList(state.invitations)
    }

    private fun generateQR(invitation: InvitationModel) {
        //generateURI
        val uri = "https://misitio.com/invitation?id=${invitation.id}&communityId=${invitation
            .communityId}&signature=${invitation.signature}"
        val bitmap: Bitmap? =
            try {
                val barcodeEncoder = BarcodeEncoder()
                 barcodeEncoder.encodeBitmap(uri, BarcodeFormat.QR_CODE, 400, 400)
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(), getString(R.string.qrException), Toast.LENGTH_LONG
                ).show()
                null
            }
        // Aquí usamos el parentFragmentManager porque estamos ya en un Fragment
        if (bitmap != null) QRGenBottomSheetDialogFragment(bitmap).show(parentFragmentManager, "qrBottomSheet")
    }
}