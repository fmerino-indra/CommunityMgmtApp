package org.fmm.communitymgmt.ui.enrollment.brothers

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.fmm.communitymgmt.domainlogic.usecase.CreateInvitationUseCase
import org.fmm.communitymgmt.domainlogic.usecase.GetInvitationsUseCase
import org.fmm.communitymgmt.domainmodels.model.InvitationModel
import org.fmm.communitymgmt.domainmodels.model.InvitationState
import org.fmm.communitymgmt.ui.security.model.UserSession
import javax.inject.Inject

@HiltViewModel
class BrothersEnrollmentViewModel @Inject constructor(
    private val getInvitations: GetInvitationsUseCase,
    private val createInvitation: CreateInvitationUseCase,
    private val userSession: UserSession

): ViewModel() {
    private var _state: MutableStateFlow<BrothersEnrollmentState>
    = MutableStateFlow (BrothersEnrollmentState.Loading)
    val state:StateFlow<BrothersEnrollmentState> get() = _state

    private lateinit var invitationList: MutableList<InvitationModel>

    fun initData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val result = getInvitations(userSession.userInfo.selectedCommunity?.myCommunityData?.id!!)
                    invitationList = result.toMutableList()
                    _state.value = BrothersEnrollmentState.Success(result)
                } catch (e: Exception) {
                    Log.e("BrothersEnrollmentViewModel", "An error has occurred", e)

                    _state.value = BrothersEnrollmentState.Error(
                        e.message ?: ("An error has " +
                                "occurred"), e)
                }
            }
        }
    }
    fun onAddInvitation(name: String, exp:Int,isMarriage:Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val invitation = createSignInvitation(name, exp.toLong(), userSession.userInfo
                .selectedCommunity!!.myCommunityData.id!!, isMarriage)
                try {
                    createInvitation(invitation)
                    initData()
                } catch (e: Exception) {
                    Log.e("BrothersEnrollmentViewModel", "An error has occurred", e)
                    _state.value = BrothersEnrollmentState.Error(
                        e.message ?: ("An error has " +
                                "occurred"), e)
                }
            }
        }
    }

    private fun createSignInvitation(name: String, exp: Long, communityId: Int, isMarriage: Boolean): InvitationModel {
        return InvitationModel(name = name, exp = exp, state =
        InvitationState.Generated, communityId = communityId, forMarriage = isMarriage)
    }

    fun filterInvitationList(generatedChecked: Boolean, processingChecked: Boolean): List<InvitationModel> {
        return invitationList.filter {
            generatedChecked && it.state == InvitationState.Generated ||
                    processingChecked && it.state == InvitationState.Processing
        }
    }
}