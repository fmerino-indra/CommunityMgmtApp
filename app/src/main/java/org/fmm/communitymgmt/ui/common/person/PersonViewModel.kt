package org.fmm.communitymgmt.ui.common.person

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.fmm.communitymgmt.domainmodels.model.Genders

class PersonViewModel(private val _formPersonState: MutableStateFlow<PersonForm>):
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
    override fun onNicknameChanged(text: String) {
        val updated = _formPersonState.value.copy(nickname = text)
        _formPersonState.value = validatePerson(updated)
    }
    override fun onBirthdayChanged(text: String) {
        _formPersonState.update {
            it.copy(birthday =  text)
        }
    }

    override fun onEmailChanged(text: String) {
        val updated = _formPersonState.value
            .copy(emailAccount = text)
        _formPersonState.value = validatePerson(updated)
    }

    // Id del RadioButton seleccionado
    override fun onGenderChanged(checked: Genders) {
        val updated = _formPersonState.value.copy(gender = checked)
        _formPersonState.value = validatePerson(updated)
    }


    /**
     * Interfaz funcional para el binding adaptaer (en fragment)
     */
    fun interface OnPersonTextChanged {
        fun onChangedText(value: String)
    }

    fun interface OnBooleanChangeFMM {
        fun onChangedBoolean(value: Boolean)
    }

    fun interface OnGenderChangeFMM {
        fun onChangeGender(value: Genders)
    }

}