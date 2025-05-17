package org.fmm.communitymgmt.data.network

import org.fmm.communitymgmt.data.network.response.CommunityDTO
import org.fmm.communitymgmt.data.network.response.CommunityListDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface CommunityListApiService {
    @GET("communities/{communityId}/brothers")
    suspend fun getCommunityList(@Path(value = "communityId") communityId : Int): CommunityListDTO

    @GET("communities")
    suspend fun getCommunities():List<CommunityDTO>
}