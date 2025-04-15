package org.fmm.communitymgmt.ui.comlist.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.fmm.communitymgmt.domainlogic.usecase.GetRelationship
import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship
import javax.inject.Inject

@HiltViewModel
class EditPersonViewModel @Inject constructor(private val useCase: GetRelationship) : ViewModel() {

    private var _state = MutableStateFlow<EditPersonState>(EditPersonState.Loading)
    val state: StateFlow<EditPersonState> = _state

    private var isEditable: Boolean = false

    init {
        _state.value = EditPersonState.Loading
    }
    fun getData(id: Int) {
        var newRelationship: AbstractRelationship?

        viewModelScope.launch {
            _state.value = EditPersonState.Loading
            val result = withContext(Dispatchers.IO) {
                newRelationship = useCase(id)
            }
            if (result!=null) {
                if (isEditable) {
                    _state.value = EditPersonState.EditMode(newRelationship!!)
                } else {
                    _state.value = EditPersonState.ViewMode(newRelationship!!)
                }
            }
        }
    }
}