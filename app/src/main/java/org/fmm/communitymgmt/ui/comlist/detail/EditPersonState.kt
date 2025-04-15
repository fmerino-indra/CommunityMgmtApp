package org.fmm.communitymgmt.ui.comlist.detail

import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship

sealed class EditPersonState {
    data object Loading: EditPersonState()
    data class Error(val error:String): EditPersonState()
    data class ViewMode(val relationship:AbstractRelationship): EditPersonState()
    data class EditMode(val relationship:AbstractRelationship): EditPersonState()

}