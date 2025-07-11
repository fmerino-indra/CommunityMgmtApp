package org.fmm.communitymgmt.domainlogic.repositories

import org.fmm.communitymgmt.domainmodels.model.FullInvitationModel
import org.fmm.communitymgmt.domainmodels.model.InvitationModel

interface InvitationRepository {
    suspend fun getInvitationList(communityId:Int):List<InvitationModel>
    suspend fun createInvitation(invitation: InvitationModel):InvitationModel
    suspend fun updateInvitation(invitation: InvitationModel):FullInvitationModel
    suspend fun getPersonalInvitation(personId: Int): List<FullInvitationModel>
    suspend fun updateSignedInvitation(invitation: FullInvitationModel):FullInvitationModel
    suspend fun acceptBrother(communityId: Int,invitationId:Int): FullInvitationModel
}