package org.fmm.communitymgmt.ui.enrollment.qrold

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
import org.fmm.communitymgmt.domainlogic.usecase.UpdateInvitationUseCase
import org.fmm.communitymgmt.domainmodels.model.InvitationModel
import org.fmm.communitymgmt.domainmodels.model.InvitationState
import org.fmm.communitymgmt.ui.security.model.UserSession
import javax.inject.Inject

@HiltViewModel
class QRReaderViewModel @Inject constructor(
    private val updateInvitation: UpdateInvitationUseCase,
    private val userSession: UserSession

): ViewModel() {
    private var _qrReaderState: MutableStateFlow<QRReaderState>
            = MutableStateFlow (QRReaderState.Scanning)
    val state:StateFlow<QRReaderState> get() = _qrReaderState

    fun initData() {
    }
    fun onInvitationRead(readUrl:String) {
        _qrReaderState.value = QRReaderState.Loading
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {

                    val result=updateInvitation(processQRUrl(readUrl))
                    initData()
                    _qrReaderState.value = QRReaderState.Success(result)
                } catch (e: Exception) {
                    Log.e("QRReaderViewModel", "An error has occurred", e)
                    _qrReaderState.value = QRReaderState.Error(
                        e.message ?: ("An error " +
                                "has occurred"), e)
                }
            }
            /*
            invitationList.add(result)
            invitationList = invitationList.toMutableList()
            _state.value = BrothersEnrollmentState.Success(invitationList)
             */
        }
    }

    private fun processQRUrl(readUrl: String): InvitationModel {
        val uri = Uri.parse(readUrl)
        Log.d("QRReaderViewModel", uri.queryParameterNames.toString())

        return createSignInvitation("prueba", uri.getQueryParameter("id")?.toInt() ?: error("Bad " +
                "QR code: id"), uri.getQueryParameter("communityId")?.toInt() ?: error("Bad QR " +
                "code: " +
                "communityId"), personId = userSession.userInfo.person.id!!
        )
    }

    private fun createSignInvitation(name: String, id:Int, communityId: Int, personId: Int):
            InvitationModel {
        return InvitationModel(name = name, id = id, state =
        InvitationState.Processing, communityId = communityId, personId = personId)
    }

}