package org.fmm.communitymgmt.ui.enrollment.invitation

import android.os.Bundle
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
import org.fmm.communitymgmt.databinding.FragmentInvitationBinding
import org.fmm.communitymgmt.ui.enrollment.invitation.recyclerview.AssignedInvitationListAdapter
import org.fmm.qr.ui.QRGenBottomSheetDialogFragment
import org.fmm.qr.ui.QRReaderBottomSheetDialogFragment

@AndroidEntryPoint
class InvitationFragment : Fragment() {
    private var _binding: FragmentInvitationBinding? = null
    private val binding get() = _binding!!
    private val invitationViewModel by viewModels<InvitationViewModel> ()

    private lateinit var qrReaderBottomDialog: QRReaderBottomSheetDialogFragment
    private lateinit var qrGenBottomDialog: QRGenBottomSheetDialogFragment

    private lateinit var assignedInvitationListAdapter: AssignedInvitationListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvitationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initUI()
    }

    private fun initUI() {
        initAdapter()
        initDialogs()
        initUIState()
        initListeners()

    }
    private fun initAdapter() {
        assignedInvitationListAdapter = AssignedInvitationListAdapter(onItemSelectedModel = {
            //@TODO Efecto de seleccionado
            invitationViewModel.selectInvitation(it)
        }, onItemDeselectedModel = {invitationViewModel.deSelectInvitation()})

        binding.rvInvitationList.apply {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = assignedInvitationListAdapter
        }
        initRefreshList()
    }


    private fun initRefreshList() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            invitationViewModel.initData()
        }
        assignedInvitationListAdapter.setCurrentSelectedIndex(-1)
    }

    private fun initData() {
        invitationViewModel.initData()
    }

    private fun initDialogs() {
        qrGenBottomDialog= QRGenBottomSheetDialogFragment()
        qrReaderBottomDialog=QRReaderBottomSheetDialogFragment({stringRead ->
            invitationViewModel.onInvitationRead(stringRead)
        })
    }

    private fun initListeners() {
        binding.btnReadInvitation.setOnClickListener {
            showQRReader()
        }
        binding.btnShowQRInvitation.setOnClickListener {
            showQR()
        }
        binding.btnSignInvitation.setOnClickListener {
            signInvitation()
        }
    }

    private fun signInvitation() {
        invitationViewModel.signInvitation()
    }

    private fun showQRReader() {
        qrReaderBottomDialog.show(parentFragmentManager, "qrBottomSheet")
    }
    private fun showQR() {
        // @TODO Revisar la generación del QR de invitación
        if (invitationViewModel.selectedInvitation != null) {
            val invitation = invitationViewModel.selectedInvitation!!
            val uri = "${requireContext().getString(R.string.intent_base_ur)}${
                requireContext()
                    .getString(R.string.intent_accept_invitation_command)
            }" +
                    "?id=${invitation.id}&communityId=${invitation
                        .communityId}&signature=${invitation.personSignature}"

            qrGenBottomDialog.uri=uri
            qrGenBottomDialog.show(parentFragmentManager, "qrBottomSheet")
        }

    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                invitationViewModel.selectedInvitationState.collect {
                    when(it) {
                        InvitationStateFlow.Scanning -> scanningState()
                        InvitationStateFlow.Loading -> loadingState()
                        is InvitationStateFlow.NotSelectedInvitation -> notSelectedInvitationState(it)
                        is InvitationStateFlow.SelectedInvitation -> selectedInvitationState(it)
                        is InvitationStateFlow.Error -> errorState(it)
                        is InvitationStateFlow.SignedInvitation -> signedInvitationState(it)
                    }
                }
            }
        }
    }

    private fun errorState(it: InvitationStateFlow.Error) {
        binding.semiLayer.isVisible=false
        binding.waiting.text=getString(R.string.invitationException)
        binding.btnReadInvitation.isVisible=false
        binding.ll1.isVisible=true
        binding.ll2.isVisible=false
        binding.ll3.isVisible=false
        message(it.errorMessage)

    }

    private fun loadingState() {
        binding.semiLayer.isVisible=true

//        binding.btnShowQRInvitation.isVisible=false
//        binding.btnReadInvitation.isVisible=false
//        binding.btnSignInvitation.isVisible=false
    }

    // 1st layout
    private fun scanningState() {
        binding.semiLayer.isVisible=false

        binding.ll1.isVisible=true
        binding.ll2.isVisible=false
        binding.ll3.isVisible=false
    }

    // 2nd layout
    private fun notSelectedInvitationState(it: InvitationStateFlow.NotSelectedInvitation) {
        binding.semiLayer.isVisible=false
        binding.ll1.isVisible=false
        binding.ll2.isVisible=true
        binding.btnSignInvitation.isEnabled=false
        binding.ll3.isVisible=false
        assignedInvitationListAdapter.updateInvitationList(it.invitationList)
        // Aquí habrá que asignar la lista al adapter
        //binding.assigned.text=getString(R.string.accept_message,it.invitation.id)
    }

    private fun selectedInvitationState(it: InvitationStateFlow.SelectedInvitation) {
        binding.semiLayer.isVisible=false
        binding.ll1.isVisible=false
        binding.ll2.isVisible=true
        binding.btnSignInvitation.isEnabled=true

        binding.ll3.isVisible=false
        // Hay que hacer el efecto
    }

    // 3rd layout
    private fun signedInvitationState(it: InvitationStateFlow.SignedInvitation) {
        binding.semiLayer.isVisible=false
        binding.ll1.isVisible=false
        binding.ll2.isVisible=false
        binding.ll3.isVisible=true
    }
    private fun message(message: String) {
        Toast.makeText(
            requireContext(), message, Toast.LENGTH_LONG
        ).show()

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
