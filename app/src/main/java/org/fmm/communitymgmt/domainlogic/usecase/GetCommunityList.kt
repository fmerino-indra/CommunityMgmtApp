package org.fmm.communitymgmt.domainlogic.usecase

import org.fmm.communitymgmt.domainlogic.repositories.CommunityListRepository
import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship
import javax.inject.Inject

class GetCommunityList @Inject constructor(private val communityListRepository:
        CommunityListRepository) {
    suspend operator fun invoke(): List<AbstractRelationship> {
        return communityListRepository.getCommunityList()
    }
}