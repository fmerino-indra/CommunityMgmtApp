package org.fmm.communitymgmt.ui.enrollment.brothers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.fmm.communitymgmt.domainlogic.usecase.GetInvitationsUseCase
import org.fmm.communitymgmt.domainlogic.usecase.SignUpUserInfoUseCase
import org.fmm.communitymgmt.domainmodels.model.CommunityModel
import org.fmm.communitymgmt.domainmodels.model.InvitationModel
import org.fmm.communitymgmt.domainmodels.model.InvitationState
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import org.fmm.communitymgmt.ui.common.AddressForm
import org.fmm.communitymgmt.ui.common.AddressViewModel
import org.fmm.communitymgmt.ui.security.model.UserSession
import javax.inject.Inject

@HiltViewModel
class BrothersEnrollmentViewModel @Inject constructor(
    private val getInvitations: GetInvitationsUseCase
): ViewModel() {
    private var _state: MutableStateFlow<BrothersEnrollmentState>
    = MutableStateFlow (BrothersEnrollmentState.Loading)
    val state:StateFlow<BrothersEnrollmentState> get() = _state

    private lateinit var invitationList: List<InvitationModel>

    fun initData() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                getInvitations()
            }
            if ((result != null)) {
                _state.value = BrothersEnrollmentState.Success(result)
            } else {
                _state.value = BrothersEnrollmentState.Error( "[FMMP] Ha ocurrido un error " +
                        "mientras se obtenían las invitaciones")
            }
        }
    }

}