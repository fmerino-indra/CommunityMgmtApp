package org.fmm.communitymgmt.domainlogic.usecase

import org.fmm.communitymgmt.domainlogic.exceptions.InvitationNotFoundException
import org.fmm.communitymgmt.domainlogic.repositories.InvitationRepository
import org.fmm.communitymgmt.domainlogic.repositories.UserInfoRepository
import org.fmm.communitymgmt.domainmodels.model.FullInvitationModel
import org.fmm.communitymgmt.domainmodels.model.InvitationModel
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import javax.inject.Inject

class GetInvitationsUseCase @Inject constructor (private val invitationRespository: InvitationRepository){
    suspend operator fun invoke(communityId:Int): List<InvitationModel> {
        return invitationRespository.getInvitationList(communityId)
    }
    suspend fun getInvitation(personId:Int):FullInvitationModel {
        val list = invitationRespository.getPersonalInvitation(personId)
        if (list.isNotEmpty())
            return list[0]
        else
            throw InvitationNotFoundException("Invitation not found")
    }
    suspend fun getInvitations(personId:Int):List<FullInvitationModel> {
        val list = invitationRespository.getPersonalInvitation(personId)
        if (list.isNotEmpty())
            return list
        else
            throw InvitationNotFoundException("Invitation not found")
    }
}