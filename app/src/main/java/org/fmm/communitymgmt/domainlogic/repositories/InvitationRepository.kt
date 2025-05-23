package org.fmm.communitymgmt.domainlogic.repositories

import org.fmm.communitymgmt.domainmodels.model.FullInvitationModel
import org.fmm.communitymgmt.domainmodels.model.InvitationModel

interface InvitationRepository {
    suspend fun getInvitationList(communityId:Int):List<InvitationModel>
    suspend fun createInvitation(invitation: InvitationModel):InvitationModel
    suspend fun updateInvitation(invitation: InvitationModel):FullInvitationModel
}