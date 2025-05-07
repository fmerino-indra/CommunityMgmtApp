package org.fmm.communitymgmt.data.network

import org.fmm.communitymgmt.data.network.response.CommunityDTO
import org.fmm.communitymgmt.data.network.response.UserInfoDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

@Deprecated("Se va a eliminar")
interface EnrollCommunityApiService {
    @PUT("/userinfo")
    suspend fun enrollCommunity(@Body userInfoDTO: UserInfoDTO): UserInfoDTO

    /**
     * Call permite manipular la petición y la respuesta, enviarlo de forma síncrona
     * o asíncrona, etc.
     * suspend fun postUserInfo(@Body userInfoDTO:UserInfoDTO): Call<UserInfoDTO>
     */

}