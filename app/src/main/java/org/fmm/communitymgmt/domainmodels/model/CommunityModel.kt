package org.fmm.communitymgmt.domainmodels.model

data class CommunityModel (
    val id: Int?,
    val communityNumber:String,
    val parish: String,
    val parishAddress: String,
    val parishAddressNumber: String,
    val parishAddressPostalCode: String,
    val parishAddressCity: String
){

}
