package org.fmm.communitymgmt.data.repositories

import android.util.Log
import androidx.collection.intSetOf
import org.fmm.communitymgmt.data.network.CommunityListApiService
import org.fmm.communitymgmt.data.network.RelationshipApiService
import org.fmm.communitymgmt.data.network.response.MarriageDTO
import org.fmm.communitymgmt.data.network.response.OtherDTO
import org.fmm.communitymgmt.data.network.response.SingleDTO
import org.fmm.communitymgmt.domainlogic.repositories.CommunityListRepository
import org.fmm.communitymgmt.domainlogic.repositories.RelationshipRepository
import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship
import org.fmm.communitymgmt.domainmodels.model.MarriageModel
import org.fmm.communitymgmt.domainmodels.model.OtherModel
import org.fmm.communitymgmt.domainmodels.model.SingleModel
import java.util.stream.Collectors
import javax.inject.Inject

class RelationshipRepositoryImpl @Inject constructor(private val apiService:
                                                     RelationshipApiService):
    RelationshipRepository {
    override suspend fun getRelationshipDetail(id: Int): AbstractRelationship? {
        runCatching {
            apiService.getRelationshipDetail(id)
        }.onSuccess {
            Log.d("[FMMP]", "Los datos: ${it.relationshipName}")
            return when(it) {
                is SingleDTO -> {
                    val aux = it.toDomain()
                    Log.d("[FMMP]", "Nickname: ${it.person.nickname}")
                    return aux
                }
                is MarriageDTO -> it.toDomain()
                else -> (it as SingleDTO).toDomain()
            }
        }.onFailure {
            Log.e("[FMMP]", "Ha ocurrido un error: \n $it")
        }
        return null
    }
}