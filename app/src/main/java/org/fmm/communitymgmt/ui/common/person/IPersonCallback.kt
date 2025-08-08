package org.fmm.communitymgmt.ui.common.person

import org.fmm.communitymgmt.domainmodels.model.Genders

interface IPersonCallback {

    fun onNameChanged(text: String)
    fun onSurname1Changed(text: String)
    fun onSurname2Changed(text: String)
    fun onEmailChanged(text: String)
    fun onNicknameChanged(text: String)
    fun onGenderChanged(checked: Genders)
    fun onBirthdayChanged(text: String)

    fun validatePerson(personForm: PersonForm): PersonForm {
        val pNameError = if (personForm.name.isBlank()) "Name is mandatory" else null
        val pS1Error = if (personForm.surname1.isBlank()) "Surname 1 is mandatory" else null
        val pS2Error = if (personForm.surname2.isBlank()) "Surname 2 is mandatory" else null
        val pEmailError = if (personForm.emailAccount.isBlank()) "eMail is mandatory" else null
        val pGenderError = if (personForm.gender == null) "Gender is mandatory" else null
//        val pBirthdayError --> Queda pendiente

        val isValid=
            pNameError == null
                    && pS1Error == null
                    && pS2Error == null
                    && pEmailError == null
                    && pGenderError == null
        return  personForm.copy(
            nameError = pNameError,
            surname1Error = pS1Error,
            surname2Error = pS2Error,
            emailAccountError = pEmailError,
            genderError = pGenderError,
            isPersonValid =  isValid)

    }
}