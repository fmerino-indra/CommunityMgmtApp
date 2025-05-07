package org.fmm.communitymgmt.ui.common

interface IAddressCallback {

    fun onAddressChanged(text: String)
    fun onAddressNumberChanged(text: String)
    fun onPostalCodeChanged(text: String)
    fun onCityChanged(text: String)
    fun validateAddress(address: AddressForm):AddressForm {
        val pAError = if (address.address.isBlank()) "Email is mandatory" else null
        val pANError = if (address.addressNumber.isBlank()) "Gender is mandatory" else null
        val pAPCError = if (address.postalCode.isBlank()) "Postal Code" +
                " is mandatory" else null
        val pACError = if (address.city.isBlank()) "City is mandatory" else null
        val isValid=
            pAError == null
            && pANError == null
            && pAPCError == null
            && pACError == null
        return  address.copy(
            addressError=pAError,
            addressNumberError = pANError,
            postalCodeError = pAPCError,
            cityError = pAPCError,
            isAddressValid = isValid)

    }
}