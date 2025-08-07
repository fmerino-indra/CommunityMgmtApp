package org.fmm.communitymgmt.ui.common.person

import kotlinx.coroutines.flow.MutableStateFlow

class PersonViewModel(private val _formPersonState: MutableStateFlow<PersonFormState>):
    IPersonCallback {

    fun initData() {
    }

    override fun onNameChanged(text: String) {
        val updated = _formPersonState.value
            .copy(name = text)
        _formPersonState.value = validatePerson(updated)
    }

    override fun onSurname1Changed(text: String) {
        val updated = _formPersonState.value
            .copy(surname1 = text)
        _formPersonState.value = validatePerson(updated)
    }

    override fun onSurname2Changed(text: String) {
        val updated = _formPersonState.value
            .copy(surname2 = text)
        _formPersonState.value = validatePerson(updated)
    }

    override fun onEmailChanged(text: String) {
        val updated = _formPersonState.value
            .copy(emailAccount = text)
        _formPersonState.value = validatePerson(updated)
    }

    /**
     * Interfaz funcional para el binding adaptaer (en fragment)
     */
    fun interface OnPersonTextChanged {
        fun onChangedText(value: String)
    }
}