package org.fmm.communitymgmt.domainmodels.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

// Mapea con SocialUser de back
data class UserInfoModel (
    // Unique internal Id
    val id:Int?,
    val name: String,
    //val familyName: String,
    val email: String,
    val emailVerified: Boolean,
    val providerId: String?,
    val provider: String,
    val imageUrl: String,
    // Relationship with Person @IgnoredOnParcel
    val person: PersonModel? = null,
    val community: CommunityModel? = null
) {
    /**
     * Not enrolled user
     */
    fun isFullEnrolled():Boolean {
        return (person != null && community != null)
    }

    /**
     * The userInfo record exists, but there isn't a community
     */
    fun isRegistering():Boolean {
        return (person != null && community == null)
    }
}