package org.fmm.communitymgmt.domainlogic.usecase

import org.fmm.communitymgmt.domainlogic.repositories.InvitationRepository
import org.fmm.communitymgmt.domainmodels.model.FullInvitationModel
import org.fmm.communitymgmt.domainmodels.model.InvitationModel
import javax.inject.Inject

class UpdateInvitationUseCase @Inject constructor (private val invitationRepository:
                                                   InvitationRepository){
    suspend operator fun invoke(invitation: InvitationModel): FullInvitationModel {
        return invitationRepository.updateInvitation(invitation)
    }
    suspend fun updateSignedInvitation(invitation: FullInvitationModel): FullInvitationModel {
        return invitationRepository.updateSignedInvitation(invitation)
    }
}