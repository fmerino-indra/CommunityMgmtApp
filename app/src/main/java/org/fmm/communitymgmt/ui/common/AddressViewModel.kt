package org.fmm.communitymgmt.ui.common

import kotlinx.coroutines.flow.MutableStateFlow

class AddressViewModel(private val _formAddressState: MutableStateFlow<AddressForm>):
    IAddressCallback {

    fun initData() {
    }

    override fun onAddressChanged(text: String) {
        val updated = _formAddressState.value
            .copy(address = text)
        _formAddressState.value = validateAddress(updated)
    }
    override fun onAddressNumberChanged(text: String) {
        val updated = _formAddressState.value
            .copy(addressNumber = text)
        _formAddressState.value = validateAddress(updated)
    }
    override fun onPostalCodeChanged(text: String) {
        val updated = _formAddressState.value
            .copy(postalCode = text)
        _formAddressState.value = validateAddress(updated)
    }
    override fun onCityChanged(text: String) {
        val updated = _formAddressState.value
            .copy(city = text)
        _formAddressState.value = validateAddress(updated)
    }
    /**
     * Interfaz funcional para el binding adaptaer (en fragment)
     */
    fun interface OnAddressTextChanged {
        fun onChangedText(value: String)
    }
}