package org.fmm.communitymgmt.data.repositories

import android.util.Log
import org.fmm.communitymgmt.data.network.InvitationApiService
import org.fmm.communitymgmt.data.network.response.InvitationDTO
import org.fmm.communitymgmt.domainlogic.repositories.InvitationRepository
import org.fmm.communitymgmt.domainmodels.model.FullInvitationModel
import org.fmm.communitymgmt.domainmodels.model.InvitationModel
import retrofit2.HttpException
import java.net.ConnectException
import javax.inject.Inject

class InvitationRepositoryImpl @Inject constructor(
    private val apiService: InvitationApiService):
    InvitationRepository {
    override suspend fun getInvitationList(communityId:Int): List<InvitationModel> {
        val list = apiService.getInvitationList(communityId)
        val newList = mutableListOf<InvitationModel>()

        list.forEach {
            newList.add(it.toDomain())
        }
        return newList
    }

    override suspend fun createInvitation(invitation: InvitationModel):
            InvitationModel {
        runCatching {
            apiService.createInvitation(invitation.communityId, InvitationDTO.fromDomain(invitation))
        }.onSuccess {
            return it.toDomain()
        }.onFailure {
            if (it is ConnectException || (it is HttpException && it.code() == 500)) {
                Log.e("InvitationRepositoryImpl", "Http Error 500", it)
                throw RuntimeException("An exception has occurred while trying to connect to server", it
                )
            } else {
                throw it
            }
        }
        throw RuntimeException("Exception while posting Invitation")
    }
    override suspend fun updateInvitation(invitation: InvitationModel):
            FullInvitationModel {
        runCatching {
            apiService.updateInvitation(invitation.communityId, InvitationDTO.fromDomain
                (invitation))
        }.onSuccess {
            return it.toDomain()
        }.onFailure {
            if (it is ConnectException || (it is HttpException && it.code() == 500)) {
                Log.e("InvitationRepositoryImpl", "Http Error 500", it)
                throw RuntimeException("An exception has occurred while trying to connect to server", it
                )
            } else {
                throw it
            }
        }
        throw RuntimeException("Exception while posting Invitation")
    }
}