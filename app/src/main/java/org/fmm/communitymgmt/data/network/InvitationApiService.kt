package org.fmm.communitymgmt.data.network

import org.fmm.communitymgmt.data.network.response.InvitationDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface InvitationApiService {
    @GET("enrollment/invitations")
    suspend fun getInvitationList(): List<InvitationDTO>

    @POST("enrollment/invitations")
    suspend fun createInvitation(@Body invitation: InvitationDTO): InvitationDTO
}