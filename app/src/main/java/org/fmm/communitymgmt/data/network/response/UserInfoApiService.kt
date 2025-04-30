package org.fmm.communitymgmt.data.network.response

import retrofit2.http.GET

interface UserInfoApiService {
    @GET("/userinfo")
    suspend fun geUserInfo(): UserInfoDTO
}