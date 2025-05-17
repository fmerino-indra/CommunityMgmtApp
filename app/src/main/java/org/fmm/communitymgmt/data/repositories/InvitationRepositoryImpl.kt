package org.fmm.communitymgmt.data.repositories

import org.fmm.communitymgmt.data.network.InvitationApiService
import org.fmm.communitymgmt.data.network.response.InvitationDTO
import org.fmm.communitymgmt.domainlogic.repositories.InvitationRepository
import org.fmm.communitymgmt.domainmodels.model.InvitationModel
import javax.inject.Inject

class InvitationRepositoryImpl @Inject constructor(
    private val apiService: InvitationApiService):
    InvitationRepository {
    override suspend fun getInvitationList(): List<InvitationModel> {
        val list = apiService.getInvitationList()
        val newList = mutableListOf<InvitationModel>()

        list.forEach {
            newList.add(it.toDomain())
        }
        return newList
    }

    override suspend fun createInvitation(invitation: InvitationModel): InvitationModel {
        var pp: InvitationDTO? = null
        try {
            pp = apiService.createInvitation(InvitationDTO.fromDomain(invitation))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return pp!!.toDomain()
    }
}