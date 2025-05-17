package org.fmm.communitymgmt.domainlogic.usecase

import org.fmm.communitymgmt.domainlogic.repositories.CommunityListRepository
import org.fmm.communitymgmt.domainmodels.model.CommunityModel
import javax.inject.Inject

class GetBelongingCommunities @Inject constructor(private val communityListRepository:
        CommunityListRepository) {
    suspend operator fun invoke(): List<CommunityModel> {
        return communityListRepository.getCommunities()
    }
}