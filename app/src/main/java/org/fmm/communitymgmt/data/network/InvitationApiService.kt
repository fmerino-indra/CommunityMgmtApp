package org.fmm.communitymgmt.data.network

import org.fmm.communitymgmt.data.network.response.FullInvitationDTO
import org.fmm.communitymgmt.data.network.response.InvitationDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface InvitationApiService {
    @GET("enrollment/{communityId}/invitations")
    suspend fun getInvitationList(@Path("communityId") communityId:Int):
            List<InvitationDTO>

    @POST("enrollment/{communityId}/invitations")
    suspend fun createInvitation(@Path("communityId") communityId:Int, @Body invitation:
    InvitationDTO): InvitationDTO

    @PUT("enrollment/{communityId}/invitations")
    suspend fun updateInvitation(@Path("communityId") communityId:Int, @Body invitation:
    InvitationDTO): FullInvitationDTO
}