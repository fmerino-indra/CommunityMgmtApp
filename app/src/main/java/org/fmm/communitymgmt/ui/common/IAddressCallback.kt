package org.fmm.communitymgmt.ui.common

interface IAddressCallback {

    fun onAddressChanged(text: String)
    fun onAddressNumberChanged(text: String)
    fun onPostalCodeChanged(text: String)
    fun onCityChanged(text: String)
    fun validateAddress(addressForm: AddressForm):AddressForm {
        val pAError = if (addressForm.address.isBlank()) "Email is mandatory" else null
        val pANError = if (addressForm.addressNumber.isBlank()) "Gender is mandatory" else null
        val pAPCError = if (addressForm.postalCode.isBlank()) "Postal Code" +
                " is mandatory" else null
        val pACError = if (addressForm.city.isBlank()) "City is mandatory" else null
        val isValid=
            pAError == null
            && pANError == null
            && pAPCError == null
            && pACError == null
        return  addressForm.copy(
            addressError=pAError,
            addressNumberError = pANError,
            postalCodeError = pAPCError,
            cityError = pAPCError,
            isAddressValid = isValid)

    }
}