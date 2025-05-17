package org.fmm.communitymgmt.data.repositories

import android.util.Log
import org.fmm.communitymgmt.data.network.CommunityListApiService
import org.fmm.communitymgmt.data.network.response.MarriageDTO
import org.fmm.communitymgmt.data.network.response.OtherDTO
import org.fmm.communitymgmt.data.network.response.SingleDTO
import org.fmm.communitymgmt.domainlogic.repositories.CommunityListRepository
import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship
import org.fmm.communitymgmt.domainmodels.model.CommunityModel
import org.fmm.communitymgmt.domainmodels.model.SingleModel
import javax.inject.Inject

class CommunityListRepositoryImpl @Inject constructor(private val apiService: CommunityListApiService):
    CommunityListRepository{
    override suspend fun getCommunityList(communityId:Int): List<AbstractRelationship> {
        val list = apiService.getCommunityList(communityId)
        val newList = mutableListOf<AbstractRelationship>()


        list.forEach {
            when (it) {
                is OtherDTO -> {
                    val aux = it.toDomain()
                    aux.forEach { it2 ->
                        newList.add(SingleModel(it.id,it2.nickname,it2))
                    }
                }
                is MarriageDTO -> newList.add(it.toDomain())
                is SingleDTO -> newList.add(it.toDomain())
            }
        }
        Log.d("[FMMP]", "El modelo de negocio ha sido creado correctamente")
        return newList
/*
        return list.stream().map {
            when (it) {
                is OtherModel -> emptyList<OtherModel>()
                else -> it.toDomain()
            }
        }.collect(Collectors.toList())

 */
    }

    override suspend fun getCommunities(): List<CommunityModel> {
        val list = apiService.getCommunities()
        return list.map {
            it.toDomain()
        }.toList()
    }
}