package org.fmm.communitymgmt.data.network

import org.fmm.communitymgmt.data.network.response.InvitationDTO
import org.fmm.communitymgmt.domainmodels.model.InvitationModel
import retrofit2.http.GET

interface InvitationApiService {
    @GET("/invitations")
    suspend fun getInvitationList(): List<InvitationDTO>
}