package org.fmm.communitymgmt.ui.enrollment.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.fmm.communitymgmt.domainlogic.usecase.SignUpUserInfoUseCase
import org.fmm.communitymgmt.domainmodels.model.EmailAccount
import org.fmm.communitymgmt.domainmodels.model.Genders
import org.fmm.communitymgmt.domainmodels.model.PersonModel
import org.fmm.communitymgmt.domainmodels.model.SocialUserInfoModel
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import org.fmm.communitymgmt.ui.common.person.PersonForm
import org.fmm.communitymgmt.ui.common.person.PersonViewModel
import org.fmm.communitymgmt.ui.security.model.UserSession
import org.fmm.communitymgmt.util.DateExtensions
import org.fmm.communitymgmt.util.toEpochDaysLong
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUserInfoUseCase: SignUpUserInfoUseCase,
    private val userSession: UserSession
):ViewModel() {

    private val _uiSignUpState = MutableStateFlow<SignUpUIState>(SignUpUIState.Loading)
    val uiSignUpSate: StateFlow<SignUpUIState> = _uiSignUpState

    private val _formSignUpState = MutableStateFlow<SignUpFormState>(SignUpFormState())
    val formSignUpState: StateFlow<SignUpFormState> = _formSignUpState

    private lateinit var _userInfoModel: UserInfoModel
    //private val userInfo get() = _userInfoModel

    private val _formPersonState = MutableStateFlow(PersonForm())
    val formPersonState: StateFlow<PersonForm> get() = _formPersonState
    val personViewModel = PersonViewModel(_formPersonState)


    // @TODO Revisar si es necesario referencia el UserInfoViewModel
    fun initData() {
        _formSignUpState.value = userSession.credential.let {
            SignUpFormState(
                personForm = PersonForm(
                    providerId = it.id,
                    name = it.displayName ?: "",
                    emailAccount = it.email,
                    gender = null
                )
            )
        }
        personViewModel. _formSignUpState.value.personForm
        _uiSignUpState.value = SignUpUIState.EditMode
    }

    /**
     * Callback for Accept Button
     */
    fun onAcceptClick() {
        viewModelScope.launch {
            _uiSignUpState.value = SignUpUIState.Loading
            val result = withContext(Dispatchers.IO) {
                val userInfo = UserInfoModel(
                    formSignUpState.value.run {
                        SocialUserInfoModel (
                            id = null,
                            name = personForm.name,
                            email = personForm.emailAccount,
                            emailVerified = false,
                            providerId = userSession.credential.id,
                            provider = userSession.credential.providerName,
                            imageUrl = userSession.credential.profilePictureUri.toString()
                        )
                    },
                    formSignUpState.value.run {
                        PersonModel(
                            id = null,
                            name = personForm.name,
                            surname1 = personForm.surname1,
                            surname2 = personForm.surname2,
                            nickname = personForm.nickname,
                            gender = if (personForm.gender == Genders.MALE) Genders.MALE else Genders.FEMALE,
                            birthday = DateExtensions.parseSpanishDate(personForm.birthday)?.toEpochDaysLong(),
                            emailAccounts = listOf(EmailAccount(emailAccount =  personForm.emailAccount))
                        )
                    }, null, emptyList(), null
                )
                runCatching {
                    signUpUserInfoUseCase(userInfo)
                }.onSuccess {
                    userSession.initialize(it)

                    _uiSignUpState.value = SignUpUIState.RegisteredMode
                    // [FMMP] Revisar userInfoViewModel
                    //_userInfoViewModel.setUserInfo(it)
                }.onFailure {
                    _uiSignUpState.value = SignUpUIState.Error(
                        "[FMMP] - Ha ocurrido un error " +
                                "en SignUpUserInfo", it
                    )
                }
            }
        }
    }

    private fun validateForm(updatedState: SignUpFormState): SignUpFormState {
        /*
        val nError = if (updatedState.name.isBlank()) "Name is mandatory" else null
        val s1Error = if (updatedState.surname1.isBlank()) "Surname1 is mandatory" else null
        val emailError = if (updatedState.emailAccount.isBlank()) "Email is mandatory" else null
        val genderError = if (updatedState.gender == null) "Gender is mandatory" else null

         */
        val isResponsibleError = if (updatedState.isResponsible == null) "Is responsible is " +
                "mandatory" else null

        /*
        val step1IsValid = nError == null
                && s1Error == null
                && genderError == null
                && emailError == null
                && isResponsibleError == null

         */

        val step1IsValid = updatedState.personForm.isPersonValid
                && isResponsibleError==null

        val aError = if (updatedState.address.isBlank()) "Address is mandatory" else null
        val aNError = if (updatedState.addressNumber.isBlank()) "Address Number is mandatory"
        else null
        val pCError = if (updatedState.postalCode.isBlank()) "Postal Code is mandatory" else null
        val cError = if (updatedState.city.isBlank()) "City is mandatory" else null

        val step2IsValid = aError == null
                && aNError == null
                && pCError == null
                && cError == null
        /*
        return updatedState.copy(
            nameError = nError?:"",
            surname1Error = s1Error?:"",
            genderError = genderError?:"",
            emailAccountError = emailError?:"",
            isResponibleError = isResponsibleError?:"",
            isStep1Valid = step1IsValid,
            addressError = aError?:"",
            addressNumberError = aNError?:"",
            postalCodeError = pCError?:"",
            cityError = cError?:"",
            isStep2Valid = step2IsValid
        )

         */
        return updatedState.copy(
            isResponibleError = isResponsibleError?:"",
            isStep1Valid = step1IsValid,
            addressError = aError?:"",
            addressNumberError = aNError?:"",
            postalCodeError = pCError?:"",
            cityError = cError?:"",
            isStep2Valid = step2IsValid
        )
    }

    /**
     * These methods are launched when field's text change from screen
     */
    /*
    fun onNameChanged(text: String) {
        val updated = _formSignUpState.value.copy(name =text)
        _formSignUpState.value = validateForm(updated)
    }
    fun onSurname1Changed(text: String) {
        val updated = _formSignUpState.value.copy(surname1 = text)
        _formSignUpState.value = validateForm(updated)
    }
    fun onSurname2Changed(text: String) {
        _formSignUpState.update {
            it.copy(surname2 =  text)
        }
    }
    fun onNicknameChanged(text: String) {
        val updated = _formSignUpState.value.copy(nickname = text)
        _formSignUpState.value = validateForm(updated)
/*
        _formSignUpState.update {
            it.copy(nickname =  text)
        }
*/
    }

    // Id del RadioButton seleccionado
    fun onGenderChanged(checked: Genders) {
        val updated = _formSignUpState.value.copy(gender = checked)
        _formSignUpState.value = validateForm(updated)
    }

    fun onBirthdayChanged(text: String) {
        _formSignUpState.update {
            it.copy(birthday =  text)
        }
    }

    fun onEmailChanged(text: String) {
        val updated = _formSignUpState.value.copy(emailAccount = text)
        _formSignUpState.value = validateForm(updated)
//        _formSignUpState.update {
//            it.copy(emailAccount =  text)
//        }
    }

     */
    fun onAddressChanged(text: String) {
        val updated = _formSignUpState.value.copy(address = text)
        _formSignUpState.value = validateForm(updated)
    }
    fun onAddressNumberChanged(text: String) {
        val updated = _formSignUpState.value.copy(addressNumber = text)
        _formSignUpState.value = validateForm(updated)
    }
    fun onPostalCodeChanged(text: String) {
        val updated = _formSignUpState.value.copy(postalCode = text)
        _formSignUpState.value = validateForm(updated)
    }
    fun onCityChanged(text: String) {
        val updated = _formSignUpState.value.copy(city = text)
        _formSignUpState.value = validateForm(updated)
    }

    fun onIsResponsibleChanged(value: Boolean) {
        val updated = _formSignUpState.value.copy(isResponsible = value)
        _formSignUpState.value = validateForm(updated)
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

    /**
     * Es necesario definir explícitamente la interfaz funcional SAM - Single Abstract Method,
     * Lo mejor sería que en el Fragment, se definiera el binding adapter así:
     *         @BindingAdapter("formFieldValue", "onFieldChanged", requireAll = false)
     *         @JvmStatic
     *         fun bindGenericaField(
     *             view: EditText, value: String?, onChanged: ((value:Genders):Unit)?
     *         ) {
     * Es decir, una función con esa definición que sería la pasada como parámetro
     */
    fun interface OnGenderChangeFMM {
        fun onChangeGender(value: Genders)
    }
}