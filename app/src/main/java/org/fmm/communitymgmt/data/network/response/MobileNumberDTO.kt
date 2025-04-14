package org.fmm.communitymgmt.data.network.response

import kotlinx.serialization.Serializable
import org.fmm.communitymgmt.domainmodels.model.MobileNumber

@Serializable
data class MobileNumberDTO (val id:Int, val mobileNumber:String) {
    fun toDomain():MobileNumber = MobileNumber(id, mobileNumber)
}