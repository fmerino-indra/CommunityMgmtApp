package org.fmm.communitymgmt.ui.home.comlist.list

import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship

sealed class CommunityListState {
    data object Loading: CommunityListState()
    data class Error(val error:String): CommunityListState()
    data class Success(val communityList:List<AbstractRelationship>): CommunityListState()
}