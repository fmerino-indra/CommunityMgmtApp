package org.fmm.communitymgmt.domainlogic.repositories

import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship

interface CommunityListRepository {
    suspend fun getCommunityList(): List<AbstractRelationship>
}