package org.fmm.communitymgmt.data.network

import org.fmm.communitymgmt.data.network.response.UserInfoDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserInfoApiService {
    @GET("userinfo/userinfo")
    suspend fun geUserInfo(): UserInfoDTO

    @POST("userinfo/userinfo")
    suspend fun postUserInfo(@Body userInfoDTO: UserInfoDTO): UserInfoDTO

    /**
     * Call permite manipular la petición y la respuesta, enviarlo de forma síncrona
     * o asíncrona, etc.
     * suspend fun postUserInfo(@Body userInfoDTO:UserInfoDTO): Call<UserInfoDTO>
     */

    @POST("userinfo/communities")
    suspend fun putUserInfo(@Body userInfoDTO: UserInfoDTO): UserInfoDTO

}