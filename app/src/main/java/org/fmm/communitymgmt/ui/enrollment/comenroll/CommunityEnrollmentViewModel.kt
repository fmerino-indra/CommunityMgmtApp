package org.fmm.communitymgmt.ui.enrollment.comenroll

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.fmm.communitymgmt.domainlogic.usecase.SignUpUserInfoUseCase
import org.fmm.communitymgmt.domainmodels.model.CommunityInfoModel
import org.fmm.communitymgmt.domainmodels.model.CommunityModel
import org.fmm.communitymgmt.ui.common.AddressForm
import org.fmm.communitymgmt.ui.common.AddressViewModel
import org.fmm.communitymgmt.ui.common.UserInfoViewModel
import javax.inject.Inject

@HiltViewModel
class CommunityEnrollmentViewModel @Inject constructor(
    private val useCase: SignUpUserInfoUseCase
    //,
    //private val _userSession:UserSession
): ViewModel() {
    private val _formCommunityEnrollmentState
            = MutableStateFlow(CommunityEnrollmentForm(isResponsible = true)
    )
    val formCommunityEnrollmentState: StateFlow<CommunityEnrollmentForm>
        get() =
        _formCommunityEnrollmentState

    private val _uiCommunityEnrollmentState
    = MutableStateFlow<CommunityEnrollmentUIState>(CommunityEnrollmentUIState.Loading)
    val uiCommunityEnrollmentSate: StateFlow<CommunityEnrollmentUIState> =
        _uiCommunityEnrollmentState


    private val _formAddressState
            = MutableStateFlow(AddressForm()
    )
    val formAddressState: StateFlow<AddressForm>
        get() =
            _formAddressState

    val addressViewModel = AddressViewModel(_formAddressState)

    private lateinit var _userInfoViewModel: UserInfoViewModel


//    private lateinit var _userInfo: UserInfoModel
//    private val userInfo get() = _userInfo

    fun initData(userInfoViewModel: UserInfoViewModel) {
        _userInfoViewModel = userInfoViewModel
  //      _userInfo = _userSession.userInfo!!
        _uiCommunityEnrollmentState.value = CommunityEnrollmentUIState.EditMode
    }

    fun onAcceptClick() {
        viewModelScope.launch {
//            _uiCommunityEnrollmentState.value = CommunityEnrollmentUIState.Loading

            val result = withContext(Dispatchers.IO) {
                val userInfo = _userInfoViewModel.userSession.userInfo.copy(_selectedCommunity =
                    formCommunityEnrollmentState.value.run {
                        CommunityInfoModel(CommunityModel(
                            id = null,
                            communityNumber = communityNumber,
                            parish = parish,
                            parishAddress = parishAddressForm.address,
                            parishAddressNumber = parishAddressForm.addressNumber,
                            parishAddressPostalCode = parishAddressForm.postalCode,
                            parishAddressCity = parishAddressForm.city,
                            isActivated = false
                        ),null)
                    }
                )
                runCatching {
                    useCase(userInfo)
                }.onSuccess {
                    // [FMMP] Esto hay que arreglarlo userInfoViewModel
                    //_userInfoViewModel.setUserInfo(it)
                    _uiCommunityEnrollmentState.value = CommunityEnrollmentUIState.RegisteredMode
                }.onFailure {
                    _uiCommunityEnrollmentState.value = CommunityEnrollmentUIState.Error(
                        "[FMMP] - Ha ocurrido un error " +
                                "en CommunityEnrollmentViewModel"
                    )
                }
            }
        }
    }

    private fun validateForm(updatedForm: CommunityEnrollmentForm): CommunityEnrollmentForm {
        val nError = if (updatedForm.communityNumber.isBlank()) "Name is mandatory" else
            null
        val pError = if (updatedForm.parish.isBlank()) "Surname1 is mandatory" else null
//        val newAddress = addressViewModel.validateAddress(updatedState.parishAddressForm)

        val isValid = nError == null
                && pError == null
                && updatedForm.parishAddressForm.isAddressValid

        return updatedForm.copy(
            nError = nError?:"",
            pError = pError?:"",
//            parishAddressForm = newAddress,
            isValid = isValid
        )
    }

    fun onCommunityNumberChanged(text: String) {
        val updated = _formCommunityEnrollmentState.value.copy(communityNumber =text)
        _formCommunityEnrollmentState.value = validateForm(updated)
    }

    fun onParishNameChanged(text: String) {
        val updated = _formCommunityEnrollmentState.value.copy(parish =text)
        _formCommunityEnrollmentState.value = validateForm(updated)
    }

    fun onIsResponsibleChanged(value: Boolean) {
        _formCommunityEnrollmentState.update {
            it.copy(isResponsible = value)
        }
    }

    fun onIsMarriedChanged(value: Boolean) {
        _formCommunityEnrollmentState.update {
            it.copy(isMarried = value)
        }
    }

    fun onAddressChanged(formAddressState: AddressForm) {
        val newForm = formCommunityEnrollmentState.value.copy(
            parishAddressForm = formAddressState
        )
        _formCommunityEnrollmentState.value = validateForm(newForm)
    }

    /**
     * Interfaz funcional para el binding adaptaer (en fragment)
     */
    fun interface OnTextChangedFMM {
        fun onChangedText(value: String)
    }
    fun interface OnBooleanChangeFMM {
        fun onChangedBoolean(value: Boolean)
    }
}