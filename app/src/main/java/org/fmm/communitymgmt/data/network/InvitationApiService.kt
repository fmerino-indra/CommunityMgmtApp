package org.fmm.communitymgmt.data.network

import kotlinx.serialization.json.JsonObject
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

    /**
     * Convendría controlar el id de la invitación, de forma que la uri fuera:
     * enrollment/{communityId}/invitations/{invitationId}
     */
    @PUT("enrollment/{communityId}/invitations")
    suspend fun updateInvitation(@Path("communityId") communityId:Int, @Body invitation:
    InvitationDTO): FullInvitationDTO

    @GET("enrollment/persons/{personId}/invitations")
    suspend fun getPersonalInvitation(@Path("personId") personId:Int):
            List<FullInvitationDTO>

    @POST("enrollment/{communityId}/invitations/{invitationId}/signature")
    suspend fun updateSignedInvitation(
        @Path("communityId") communityId:Int,
        @Path("invitationId") invitationId:Int,
        @Body json:JsonObject
    ): FullInvitationDTO

    @POST("enrollment/{communityId}/invitations/{invitationId}/accept")
    suspend fun acceptBrother(
        @Path("communityId") communityId:Int,
        @Path("invitationId") invitationId:Int
    ): FullInvitationDTO

}