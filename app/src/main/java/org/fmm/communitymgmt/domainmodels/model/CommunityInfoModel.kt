package org.fmm.communitymgmt.domainmodels.model

data class CommunityInfoModel(
    val myCommunityData: CommunityModel,
    val myCharges:List<TChargeModel>?
) {
}