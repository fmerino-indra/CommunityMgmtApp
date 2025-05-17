package org.fmm.communitymgmt.ui.home.comlist.list

import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship
import org.fmm.communitymgmt.domainmodels.model.CommunityModel

sealed class CommunitySelectState {
    data object NotSelected: CommunitySelectState()
    data class Error(val error:String): CommunitySelectState()
    data class Selected(val community:CommunityModel): CommunitySelectState()
}