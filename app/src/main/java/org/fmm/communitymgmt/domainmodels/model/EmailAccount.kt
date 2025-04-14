package org.fmm.communitymgmt.domainmodels.model

import kotlinx.serialization.Serializable

@Serializable
data class EmailAccount (val id:Int, val emailAccount:String) {
}