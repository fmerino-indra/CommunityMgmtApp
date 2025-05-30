package org.fmm.communitymgmt.data.network

import org.fmm.communitymgmt.data.network.response.AbstractRelationshipDTO
import org.fmm.communitymgmt.data.network.response.CommunityListDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RelationshipApiService {
    @GET("/brothers/{relationshipId}")
    suspend fun getRelationshipDetail(@Path("relationshipId") relationshipId:Int): AbstractRelationshipDTO

    @PUT("/brothers/{relationshipId}")
    suspend fun editRelationshipDetail(@Path("relationshipId") relationshipId:Int, @Body
    relationship: AbstractRelationshipDTO):
            AbstractRelationshipDTO
}