package org.fmm.communitymgmt.data.repositories

import android.util.Log
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.fmm.communitymgmt.data.network.InvitationApiService
import org.fmm.communitymgmt.data.network.response.FullInvitationDTO
import org.fmm.communitymgmt.data.network.response.InvitationDTO
import org.fmm.communitymgmt.domainlogic.exceptions.InvitationNotFoundException
import org.fmm.communitymgmt.domainlogic.repositories.InvitationRepository
import org.fmm.communitymgmt.domainmodels.model.FullInvitationModel
import org.fmm.communitymgmt.domainmodels.model.InvitationModel
import retrofit2.HttpException
import java.net.ConnectException
import java.util.stream.Collectors
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

    override suspend fun getPersonalInvitation(personId: Int): List<FullInvitationModel> {
        runCatching {
            apiService.getPersonalInvitation(personId)
        }.onSuccess { it ->
            return it.stream()
                .map {dto-> dto.toDomain()}
                .collect(Collectors.toList())
        }.onFailure {
            if (it is ConnectException || (it is HttpException && it.code() == 500)) {
                Log.e("InvitationRepositoryImpl", "Http Error 500", it)
                throw RuntimeException("An exception has occurred while trying to connect to server", it
                )
            } else if (it is HttpException && it.code() == 404) {
                Log.d("InvitationRepositoryImpl", "Http Error 404", it)
                throw InvitationNotFoundException("Invitation not found")
            } else {
                throw it
            }
        }
        throw RuntimeException("Exception while posting Invitation")

    }

    override suspend fun updateSignedInvitation(invitation: FullInvitationModel): FullInvitationModel {
        runCatching {

            val json = buildJsonObject {
                put("signature", invitation.personSignature!!)
                put("publicKey", invitation.personPublicKey)
            }
            apiService.updateSignedInvitation(
                communityId =  invitation.communityId,
                invitationId = invitation.id!!,
                json
            )
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
    override suspend fun acceptBrother(communityId: Int,invitationId:Int): FullInvitationModel {
        runCatching {

            apiService.acceptBrother(
                communityId =  communityId,
                invitationId = invitationId
            )
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
        throw RuntimeException("Exception while accepting brother")
    }
}