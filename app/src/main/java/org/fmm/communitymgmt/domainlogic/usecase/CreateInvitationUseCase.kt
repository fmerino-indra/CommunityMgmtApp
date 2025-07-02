package org.fmm.communitymgmt.domainlogic.usecase

import org.fmm.communitymgmt.domainlogic.repositories.InvitationRepository
import org.fmm.communitymgmt.domainmodels.model.FullInvitationModel
import org.fmm.communitymgmt.domainmodels.model.InvitationModel
import javax.inject.Inject

class CreateInvitationUseCase @Inject constructor (private val invitationRepository:
                                                   InvitationRepository){
    suspend operator fun invoke(invitation: InvitationModel): InvitationModel {
        return invitationRepository.createInvitation(invitation)
    }
    suspend fun acceptBrother(communityId:Int, invitationId:Int): FullInvitationModel {
        return invitationRepository.acceptBrother(communityId, invitationId)
    }
}