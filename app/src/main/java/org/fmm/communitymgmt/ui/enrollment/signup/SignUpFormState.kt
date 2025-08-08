package org.fmm.communitymgmt.ui.enrollment.signup

import org.fmm.communitymgmt.ui.common.person.PersonForm

// @todo Actualizar los campos con los correspondientes
// @todo Pintar la pantalla
data class SignUpFormState (
    val personForm: PersonForm = PersonForm(),
    /*
    val providerId : String? = "",
    val name: String = "",
    val surname1: String = "",
    val surname2: String = "",
    val nickname: String = "",
    val gender: Genders? = null,
    val birthday: String = "",
    val emailAccount: String = "",

     */
    val isResponsible: Boolean? = false,

    /*
    val nameError: String = "",
    val surname1Error: String = "",
    val surname2Error: String = "",
    val nicknameError: String = "",
    val genderError: String = "",
    val emailAccountError: String = "",

     */
    val isResponibleError: String = "",

    val isStep1Valid:Boolean = false,

    val phoneNumber: String = "",

    val address: String = "",
    val addressNumber: String = "",
    val postalCode: String = "",
    val city: String = "",

    val addressError: String = "",
    val addressNumberError: String = "",
    val postalCodeError: String = "",
    val cityError: String = "",


    val isStep2Valid:Boolean = false,



    val phoneNumberError: String = "",

    var currentStep: Int = 0
//    val userInfo: UserInfoModel?
) {

    fun isValid() : Boolean {
        return if (currentStep == 0)
            isStep1Valid
        else if (currentStep == 1)
            isStep2Valid
        else
            false
    }
}

