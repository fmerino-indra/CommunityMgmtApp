package org.fmm.communitymgmt.domainlogic.repositories

import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship
import org.fmm.communitymgmt.domainmodels.model.CommunityModel

interface CommunityListRepository {
    suspend fun getCommunityList(communityId:Int): List<AbstractRelationship>
    suspend fun getCommunities(): List<CommunityModel>
}