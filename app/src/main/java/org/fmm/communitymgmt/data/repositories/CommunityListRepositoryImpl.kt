package org.fmm.communitymgmt.data.repositories

import org.fmm.communitymgmt.data.network.CommunityListApiService
import org.fmm.communitymgmt.domainlogic.repositories.CommunityListRepository
import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship
import java.util.stream.Collectors
import javax.inject.Inject

class CommunityListRepositoryImpl @Inject constructor(private val apiService: CommunityListApiService):
    CommunityListRepository{
    override suspend fun getCommunityList(): List<AbstractRelationship> {
        val list = apiService.getCommunityList()
        return list.stream().map {
            it.toDomain()
        }.collect(Collectors.toList())
    }
}