package org.fmm.communitymgmt.domainmodels.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

// Mapea con SocialUser de back
@Parcelize
data class UserInfoModel (
    // Unique internal Id
    val id:Int,
    // Unique IdP functional Id
//    val upn:String,
    // Unique IdP internal Id (IdPUserId)
    val userId:String,
    val name: String,
    val email: String,
    val emailVerified: Boolean,
    val providerId: Int,
    val provider: String,
    val imageUrl: String?,
    // Relationship with Person
    @IgnoredOnParcel val person: PersonModel? = null
): Parcelable {

}