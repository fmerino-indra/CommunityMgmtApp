package org.fmm.communitymgmt.data.network.response

import kotlinx.serialization.Serializable
import org.fmm.communitymgmt.domainmodels.model.EmailAccount

@Serializable
data class EmailAccountDTO (val id:Int?=null, val emailAccount:String) {
    fun toDomain() = EmailAccount(id, emailAccount)

    companion object {
        fun fromDomain(emailAccount: EmailAccount) =
            with(emailAccount) {
                EmailAccountDTO(id, this.emailAccount)
            }
    }
}