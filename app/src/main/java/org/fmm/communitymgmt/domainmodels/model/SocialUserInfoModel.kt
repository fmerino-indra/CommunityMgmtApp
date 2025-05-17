package org.fmm.communitymgmt.domainmodels.model

// Mapea con SocialUser de back
data class SocialUserInfoModel (
    // Unique internal Id
    val id:Int?,
    val name: String,
    //val familyName: String,
    val email: String,
    val emailVerified: Boolean,
    val providerId: String?,
    val provider: String,
    val imageUrl: String,
) {
}