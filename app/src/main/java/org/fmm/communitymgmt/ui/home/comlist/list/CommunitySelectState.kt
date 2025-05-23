package org.fmm.communitymgmt.ui.home.comlist.list

import org.fmm.communitymgmt.domainmodels.model.CommunityInfoModel

sealed class CommunitySelectState {
    data object NotSelected: CommunitySelectState()
    data class Error(val error:String): CommunitySelectState()
    data class Selected(val community:CommunityInfoModel): CommunitySelectState()
}