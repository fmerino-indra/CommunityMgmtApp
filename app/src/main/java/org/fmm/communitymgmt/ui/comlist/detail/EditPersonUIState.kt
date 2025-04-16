package org.fmm.communitymgmt.ui.comlist.detail

import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship

sealed class EditPersonUIState {
    data object Loading: EditPersonUIState()
    data class Error(val error:String): EditPersonUIState()
    data class ViewMode(val relationship:AbstractRelationship): EditPersonUIState()
    data class EditMode(val relationship:AbstractRelationship): EditPersonUIState()

}