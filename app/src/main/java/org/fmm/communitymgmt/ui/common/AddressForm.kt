package org.fmm.communitymgmt.ui.common

data class AddressForm (
    val address: String = "",
    val addressNumber: String = "",
    val postalCode: String = "",
    val city: String = "",

    val addressError: String? = "",
    val addressNumberError: String? = "",
    val postalCodeError: String? = "",
    val cityError: String? = "",

    val isAddressValid:Boolean = false,
){
}