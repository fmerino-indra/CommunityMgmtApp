package org.fmm.communitymgmt.domainmodels.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class EmailAccount (val id:Int?=null, val emailAccount:String): Parcelable {
}