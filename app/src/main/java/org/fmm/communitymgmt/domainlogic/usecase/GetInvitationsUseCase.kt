package org.fmm.communitymgmt.domainlogic.usecase

import org.fmm.communitymgmt.domainlogic.repositories.InvitationRepository
import org.fmm.communitymgmt.domainlogic.repositories.UserInfoRepository
import org.fmm.communitymgmt.domainmodels.model.InvitationModel
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import javax.inject.Inject

class GetInvitationsUseCase @Inject constructor (private val invitationRespository: InvitationRepository){
    suspend operator fun invoke(communityId:Int): List<InvitationModel> {
        return invitationRespository.getInvitationList(communityId)
    }
}