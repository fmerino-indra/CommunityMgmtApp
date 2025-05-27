package org.fmm.communitymgmt.ui.enrollment.invitation

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.fmm.communitymgmt.domainlogic.exceptions.InvitationNotFoundException
import org.fmm.communitymgmt.domainlogic.usecase.GetInvitationsUseCase
import org.fmm.communitymgmt.domainlogic.usecase.UpdateInvitationUseCase
import org.fmm.communitymgmt.domainmodels.model.FullInvitationModel
import org.fmm.communitymgmt.domainmodels.model.InvitationModel
import org.fmm.communitymgmt.domainmodels.model.InvitationState
import org.fmm.communitymgmt.ui.security.model.UserSession
import javax.inject.Inject

@HiltViewModel
class InvitationViewModel @Inject constructor(
    private val updateInvitation: UpdateInvitationUseCase,
    private val getInvitationsUseCase: GetInvitationsUseCase,
    private val userSession: UserSession

): ViewModel() {
    //@TODO FMMP -> Por aquí 24/05/2025
    private var _selectedInvitationState: MutableStateFlow<InvitationStateFlow>
            = MutableStateFlow (InvitationStateFlow.Scanning)
    val selectedInvitationState:StateFlow<InvitationStateFlow> get() = _selectedInvitationState

    private lateinit var invitationList: List<FullInvitationModel>
    private var _selectedInvitation:FullInvitationModel?=null
    val selectedInvitation get() = _selectedInvitation
/*
    private var _selectedInvitation: MutableStateFlow<InvitationStateFlow> = MutableStateFlow (InvitationStateFlow.Scanning)
    val selectedInvitation:StateFlow<InvitationStateFlow> get() = _selectedInvitation

 */
    fun initData() {
        _selectedInvitationState.value = InvitationStateFlow.Loading
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    invitationList = getInvitationsUseCase
                        .getInvitations(userSession.userInfo.person.id!!)
                    if (invitationList.size == 1) {
                        _selectedInvitation = invitationList[0]
                        _selectedInvitationState.value =
                            InvitationStateFlow.SelectedInvitation(invitationList[0])
                    } else if (invitationList.size > 1) {
                        _selectedInvitation = null
                        _selectedInvitationState.value =
                            InvitationStateFlow.NotSelectedInvitation(invitationList)
                    }

                } catch (e: Exception) {
                    if (e is InvitationNotFoundException) {
                        _selectedInvitationState.value = InvitationStateFlow.Scanning
                    }
                    Log.e("QRReaderViewModel", "An error has occurred", e)
                    _selectedInvitationState.value = InvitationStateFlow.Error(
                        e.message ?: ("An error " +
                                "has occurred"), e
                    )
                }
            }
        }
    }
    fun onInvitationRead(readUrl:String) {
        _selectedInvitationState.value = InvitationStateFlow.Loading
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {

                    val result=updateInvitation(processQRUrl(readUrl))
                    initData()
                    _selectedInvitation = result
                    _selectedInvitationState.value = InvitationStateFlow.SelectedInvitation(result)
                } catch (e: Exception) {
                    Log.e("QRReaderViewModel", "An error has occurred", e)
                    _selectedInvitationState.value = InvitationStateFlow.Error(
                        e.message ?: ("An error " +
                                "has occurred"), e
                    )
                }
            }
        }
    }

    fun signInvitation() {
        if (selectedInvitationState.value is InvitationStateFlow.SelectedInvitation) {
            val selInvitation = (selectedInvitationState.value as InvitationStateFlow
                .SelectedInvitation).invitation
            _selectedInvitationState.value = InvitationStateFlow.Loading
            viewModelScope.launch {
                val result = withContext(Dispatchers.IO) {
                    try {
                        val invitationSigned=updateInvitation.updateSignedInvitation(
                            selInvitation.copy(
                                personSignature = generateInvitationSignature(selInvitation),
                                personPublicKey = getPublicKey()
                            )
                        )
                        _selectedInvitation = invitationSigned
                        _selectedInvitationState.value = InvitationStateFlow.SignedInvitation(invitationSigned)
                    } catch (e: Exception) {
                        Log.e("QRReaderViewModel", "An error has occurred", e)
                        _selectedInvitationState.value = InvitationStateFlow.Error(
                            e.message ?: ("An error " +
                                    "has occurred"), e
                        )
                    }
                }

            }
        }
    }
    fun selectInvitation(invitationSelected: FullInvitationModel) {
        _selectedInvitationState.value = InvitationStateFlow.SelectedInvitation(invitationSelected)
    }
    fun deSelectInvitation() {
        _selectedInvitationState.value = InvitationStateFlow.NotSelectedInvitation(invitationList)
    }

    private fun processQRUrl(readUrl: String): InvitationModel {
        val uri = Uri.parse(readUrl)
        Log.d("QRReaderViewModel", uri.queryParameterNames.toString())

        return createInvitationToSign("prueba", uri.getQueryParameter("id")?.toInt() ?: error("Bad " +
                "QR code: id"), uri.getQueryParameter("communityId")?.toInt() ?: error("Bad QR " +
                "code: " +
                "communityId"), personId = userSession.userInfo.person.id!!
        )
    }

    private fun createInvitationToSign(name: String, id:Int, communityId: Int, personId: Int):
            InvitationModel {
        return InvitationModel(name = name, id = id, state =
        InvitationState.Processing, communityId = communityId, personId = personId)
    }

    /**
     * @TODO [FMMP] Cryptography
     */
    private fun generateInvitationSignature(invitation: FullInvitationModel): String {
        return "This is a Brother Signature"
    }

    private fun getPublicKey(): String {
        return "This is a Brother Public Key"
    }
}