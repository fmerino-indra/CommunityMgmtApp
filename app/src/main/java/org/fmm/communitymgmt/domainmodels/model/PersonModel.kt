package org.fmm.communitymgmt.domainmodels.model

import java.time.LocalDate

data class PersonModel(
    val name: String,
    val surname1: String?,
    val surname2: String? = null,
    val nickname: String,
    val emailAccount: String? = null,
    val birthday: LocalDate,
    val gender: Genders
    //,
//    val image: String? = null
    ) {
}