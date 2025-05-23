package org.fmm.communitymgmt.domainlogic.usecase

import org.fmm.communitymgmt.domainlogic.repositories.InvitationRepository
import org.fmm.communitymgmt.domainlogic.repositories.UserInfoRepository
import org.fmm.communitymgmt.domainmodels.model.FullInvitationModel
import org.fmm.communitymgmt.domainmodels.model.InvitationModel
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import javax.inject.Inject

class UpdateInvitationUseCase @Inject constructor (private val invitationRespository:
                                                   InvitationRepository){
    suspend operator fun invoke(invitation: InvitationModel): FullInvitationModel {
        return invitationRespository.updateInvitation(invitation)
    }
}