package org.fmm.communitymgmt.ui.enrollment.brothers

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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.databinding.FragmentBrothersEnrollmentBinding
import org.fmm.communitymgmt.domainmodels.model.InvitationModel
import org.fmm.communitymgmt.domainmodels.model.InvitationState
import org.fmm.communitymgmt.ui.enrollment.brothers.dialog.AddInvitationDialog
import org.fmm.communitymgmt.ui.enrollment.brothers.recyclerview.InvitationListAdapter
import org.fmm.qr.ui.QRGenBottomSheetDialogFragment
import org.fmm.qr.ui.QRReaderBottomSheetDialogFragment

@AndroidEntryPoint
class BrothersEnrollmentFragment : Fragment() {
    private val brothersEnrollmentViewModel by viewModels<BrothersEnrollmentViewModel> ()

    private var _binding: FragmentBrothersEnrollmentBinding? = null
    private val binding get() = _binding!!

    private var generatedChecked = true
    private var processingChecked = true

    private lateinit var invitationListAdapter: InvitationListAdapter

    private lateinit var qrReaderBottomDialog: QRReaderBottomSheetDialogFragment
    private lateinit var qrGenBottomDialog: QRGenBottomSheetDialogFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBrothersEnrollmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initData()
    }

    private fun initData() {
        brothersEnrollmentViewModel.initData()
    }

    private fun initUI() {
        initButtons()
        initAdapter()
        initUIState()
        initListeners()
        initDialogs()
    }

    private fun initButtons() {
        generatedChecked = binding.tgGenerated.isChecked
        processingChecked = binding.tgProcessing.isChecked
    }

    private fun initListeners() {
        binding.fabAddInvitation.setOnClickListener {
            showAddInvitationDialog()
        }
        binding.tgGenerated.setOnClickListener {
            onGeneratedChange(binding.tgGenerated.isChecked)
        }
        binding.tgProcessing.setOnClickListener {
            onProcessingChange(binding.tgProcessing.isChecked)
        }
    }

    private fun onGeneratedChange(checked: Boolean) {
        generatedChecked = checked
        filterInvitationList()
    }

    private fun onProcessingChange(checked: Boolean) {
        processingChecked = checked
        filterInvitationList()
    }

    private fun showAddInvitationDialog() {
        val fragmentManager = parentFragmentManager
        val newFragment = AddInvitationDialog(this.brothersEnrollmentViewModel)

        newFragment.show(fragmentManager, "dialog")
    }

    private fun initAdapter() {
        invitationListAdapter = InvitationListAdapter(onItemSelected = {
            if (it.state == InvitationState.Generated)
                generateQR(it)
            else {
                // Navegando a otra página
                //navigateToQR()
                showQRReader()
            }
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
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            brothersEnrollmentViewModel.initData()
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                brothersEnrollmentViewModel.brothersEnrollmentFlow.collect {
                    when(it) {
                        is BrothersEnrollmentState.Error -> errorState(it)
                        BrothersEnrollmentState.Loading -> loadingState()
                        is BrothersEnrollmentState.Success -> successState(it)
                    }
                }
            }
        }
    }

    private fun errorState(brothersEnrollmentState: BrothersEnrollmentState.Error) {
        binding.progressBar.isVisible = false
        Log.e("[BrothersEnrollmentFragment]", brothersEnrollmentState.errorMessage,
            brothersEnrollmentState.exception)
        message(brothersEnrollmentState.errorMessage)
    }

    private fun loadingState() {
        binding.progressBar.isVisible = true
    }

    private fun successState(state: BrothersEnrollmentState.Success) {
        binding.progressBar.isVisible = false
        filterInvitationList()
    }
    private fun filterInvitationList() {
        val selectedInvitations = brothersEnrollmentViewModel
            .filterInvitationList(generatedChecked,processingChecked)
        invitationListAdapter.updateInvitationList(selectedInvitations)

    }

    private fun generateQR(invitation: InvitationModel) {

        val uri = "${requireContext().getString(R.string.intent_base_ur)}${requireContext()
            .getString(R.string.intent_invitation_command)}" +
                "?id=${invitation.id}&communityId=${invitation.communityId}&signature=${invitation.signature}"
// @TODO Pendiente generar el QR del Brother para que lo lea el responsable: JSON
        qrGenBottomDialog.uri = uri
        qrGenBottomDialog.show(parentFragmentManager, "qrBottomSheet")
    }

    private fun message(message: String) {
        Toast.makeText(
            requireContext(), message, Toast.LENGTH_LONG
        ).show()

    }

    /**
     * Methods for Read Accept Invitations QR
     */

    private fun initDialogs() {
        qrGenBottomDialog = QRGenBottomSheetDialogFragment()
        qrReaderBottomDialog=QRReaderBottomSheetDialogFragment({
            onReadQRReaderDialog(it)
        })
    }

    private fun onReadQRReaderDialog(qr:String) {
        qrReaderBottomDialog.dismiss()
        brothersEnrollmentViewModel.onQRRead(qr)

    }

    private fun showQRReader() {
        qrReaderBottomDialog.show(parentFragmentManager, "qrBottomSheet")
    }

}