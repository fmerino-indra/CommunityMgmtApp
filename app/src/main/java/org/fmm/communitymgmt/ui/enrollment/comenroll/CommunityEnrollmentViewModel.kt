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
import org.fmm.communitymgmt.domainmodels.model.CommunityModel
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import org.fmm.communitymgmt.ui.security.model.UserSession
import javax.inject.Inject

@HiltViewModel
class CommunityEnrollmentViewModel @Inject constructor(
    private val useCase: SignUpUserInfoUseCase,
    private val _userSession:UserSession
): ViewModel() {
    private val _formCommunityEnrollmentState
            = MutableStateFlow(CommunityEnrollmentFormState()
    )
    val formCommunityEnrollmentState: StateFlow<CommunityEnrollmentFormState>
        get() =
        _formCommunityEnrollmentState

    private val _uiCommunityEnrollmentState
    = MutableStateFlow<CommunityEnrollmentUIState>(CommunityEnrollmentUIState.Loading)
    val uiCommunityEnrollmentSate: StateFlow<CommunityEnrollmentUIState> =
        _uiCommunityEnrollmentState


    private lateinit var _userInfo: UserInfoModel
    private val userInfo get() = _userInfo

    fun initData() {
        _userInfo = _userSession.userInfo!!
        _uiCommunityEnrollmentState.value = CommunityEnrollmentUIState.EditMode
//        if (!isResponsible)
//            throw RuntimeException("Only Responsible")
//        _formCommunityEnrollmentState.value = CommunityEnrollmentFormState(isResponsible = true)
    }

    fun onAcceptClick() {
        viewModelScope.launch {
            _uiCommunityEnrollmentState.value = CommunityEnrollmentUIState.Loading

            val result = withContext(Dispatchers.IO) {
                val userInfo = _userInfo.copy(community =
                    formCommunityEnrollmentState.value.run {
                        CommunityModel(
                            id = null,
                            communityNumber = communityNumber,
                            parish = parish,
                            parishAddress = parishAddress,
                            parishAddressNumber = parishAddressNumber,
                            parishAddressPostalCode = parishAddressPostalCode,
                            parishAddressCity = parishAddressCity
                        )
                    }
                )
                runCatching {
                    useCase(userInfo)
                }.onSuccess {
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

    private fun validateForm(updatedState: CommunityEnrollmentFormState): CommunityEnrollmentFormState {
        val nError = if (updatedState.communityNumber.isBlank()) "Name is mandatory" else
            null
        val pError = if (updatedState.parish.isBlank()) "Surname1 is mandatory" else null
        val pAError = if (updatedState.parishAddress.isBlank()) "Email is mandatory" else null
        val pANError = if (updatedState.parishAddressNumber.isBlank()) "Gender is mandatory" else null
        val pAPCError = if (updatedState.parishAddressPostalCode.isBlank()) "Postal Code" +
                " is mandatory" else null
        val pACError = if (updatedState.parishAddressCity.isBlank()) "City is mandatory" else null

        val isValid = nError == null
                && pError == null
                && pAError == null
                && pANError == null
                && pAPCError == null
                && pACError == null

        return updatedState.copy(
            nError = nError?:"",
            pError = pError?:"",
            pAError = pAError ?:"",
            pANError = pANError?:"",
            pAPCError = pAPCError?:"",
            pACError = pACError?:"",
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
    fun onAddressChanged(text: String) {
        val updated = _formCommunityEnrollmentState.value.copy(parishAddress =text)
        _formCommunityEnrollmentState.value = validateForm(updated)
    }
    fun onAddressNumberChanged(text: String) {
        val updated = _formCommunityEnrollmentState.value.copy(parishAddressNumber =text)
        _formCommunityEnrollmentState.value = validateForm(updated)
    }
    fun onPostalCodeChanged(text: String) {
        val updated = _formCommunityEnrollmentState.value.copy(parishAddressPostalCode =text)
        _formCommunityEnrollmentState.value = validateForm(updated)
    }
    fun onCityChanged(text: String) {
        val updated = _formCommunityEnrollmentState.value.copy(parishAddressCity =text)
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