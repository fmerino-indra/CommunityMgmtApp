package org.fmm.communitymgmt.domainlogic.repositories

import org.fmm.communitymgmt.domainmodels.model.InvitationModel

interface InvitationRepository {
    suspend fun getInvitationList():List<InvitationModel>
    suspend fun createInvitation(invitation: InvitationModel):InvitationModel
}