package org.fmm.communitymgmt.ui.comlist.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.fmm.communitymgmt.domainlogic.usecase.GetRelationship
import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship
import org.fmm.communitymgmt.domainmodels.model.SingleModel
import javax.inject.Inject

@HiltViewModel
class EditPersonViewModel @Inject constructor(private val useCase: GetRelationship) : ViewModel() {

    private var _uiState = MutableStateFlow<EditPersonUIState>(EditPersonUIState.Loading)
    val uiState: StateFlow<EditPersonUIState> = _uiState

    private var _formState = MutableStateFlow<EditPersonFormState>(EditPersonFormState())
    val formState: StateFlow<EditPersonFormState> = _formState

    private var isEditable: Boolean = true

    init {
        _uiState.value = EditPersonUIState.Loading
    }
    fun getData(id: Int) {
        var newRelationship: AbstractRelationship?

        viewModelScope.launch {
            _uiState.value = EditPersonUIState.Loading
            val result = withContext(Dispatchers.IO) {
                newRelationship = useCase(id)
                Log.d("[FMMP]", "El valor del objeto newRelationship: \n $newRelationship")
                // Ahora hay que transformar la nueva relación en el modelo que usa el formulario.
                if (newRelationship is SingleModel) {

                    _formState.value = (newRelationship as SingleModel).run {
                         EditPersonFormState(
                             name = person.name,
                             surname1 = person.surname1?:"",
                             surname2 = person.surname2?:"",
                             nickname = person.nickname)
                    }

                }
            }
            if (result!=null) {
                if (isEditable) {
                    _uiState.value = EditPersonUIState.EditMode(newRelationship!!)
                } else {
                    _uiState.value = EditPersonUIState.ViewMode(newRelationship!!)
                }
            }
        }
    }

    /**
     * Como he usado two way data-binding, ya tiene los cambios. No hay que pasarle nada
     */
    fun onAcceptClick() {
        Log.d("[FMMP] [EditPersonViewModel - onAcceptClick]", "Se ha pulsado el botón Aceptar\n" +
                "Los datos de que dispongo: \nrelationship=${this.formState.value}" +
                "\nUIState=${this.uiState}" +
                "\nUIState.value (lo que traje de bbdd)=${this.uiState.value}")

    }

    private fun validateForm(updatedState: EditPersonFormState): EditPersonFormState {
        val nError = if (updatedState.name.isBlank()) "Name is mandatory" else null
        val s1Error = if (updatedState.surname1.isBlank()) "Surname1 is mandatory" else null
        val isValid = nError == null && s1Error == null

        return updatedState.copy(
            nameError = nError?:"",
            surname1Error = s1Error?:"",
            isValid = isValid
        )
    }
    fun onNameChanged(text:String)  {
        if (text == _formState.value.name) {
            Log.d("[FMMP] [EditPersonViewModel - onNameChanged]", "Recibido cambio de EditText. " +
                    "Saliendo")
            return
        }
        Log.d("[FMMP] [EditPersonViewModel - onNameChanged]", "Recibido cambio de EditText Name\n" +
                "newValue=${text}")
        val updatedState = _formState.value.copy(name = text)
        _formState.value = validateForm(updatedState)
    }

    fun onSurname1Changed(text: String) {
        Log.d("[FMMP] [EditPersonViewModel - onNameChanged]", "Recibido cambio de EditText " +
                "Surname1\n" +
                "newValue=${text}")
        val updated = _formState.value.copy(surname1 = text)
        _formState.value = validateForm(updated)
    }
    fun onSurname2Changed(text: String) {
        _formState.update {
            it.copy(surname2 =  text)
        }
    }

/*
    Definimos un interfaz funcional porque la forma en BindingAdapter:
        fun bindSurname1(
            view: EditText, state: EditPersonFormState?, onChanged: EditPersonViewModel
            .OnTextChangedFMM?) {...

    Al ser llamada en el XML: app:onNameChanged="@{viewModelFMM::onNameChanged}"
    Da error de compilación

 */

    fun interface OnTextChangedFMM {
        fun onChangedFMM(value: String)
    }
}