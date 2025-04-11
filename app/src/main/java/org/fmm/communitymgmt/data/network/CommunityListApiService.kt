package org.fmm.communitymgmt.data.network

import org.fmm.communitymgmt.data.network.response.CommunityListDTO
import retrofit2.http.GET

interface CommunityListApiService {
    @GET("/brothers")
    suspend fun getCommunityList(): CommunityListDTO
}