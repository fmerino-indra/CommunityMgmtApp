package org.fmm.communitymgmt.ui.common.person

import org.fmm.communitymgmt.domainmodels.model.Genders

data class PersonForm (
    val providerId: String = "",
    val name: String = "",
    val surname1: String = "",
    val surname2: String = "",
    val nickname: String = "",
    val gender: Genders? = null,
    val birthday: String = "",
    val emailAccount: String = "",
    val isResponsible: Boolean? = false,

    val providerIdError: String? = "",
    val nameError: String? = "",
    val surname1Error: String? = "",
    val surname2Error: String? = "",
    val nicknameError: String? = "",
    val genderError: String? = "",
    val emailAccountError: String? = "",
    val isResponibleError: String? = "",

    val isPersonValid:Boolean = false,
){
}