package org.fmm.communitymgmt.data.network.response

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.fmm.communitymgmt.domainmodels.model.SingleModel

@Serializable
@SerialName("Single")
data class SingleDTO(
    override val id: Int,
    override val relationshipName: String,
    val person: PersonDTO,

    ) : AbstractRelationshipDTO() {

    override fun toDomain() = SingleModel(id, relationshipName, person.toDomain())

    companion object {
        fun fromDomain(model: SingleModel) = model.run {
            SingleDTO(
                id = this.id, relationshipName = this.relationshipName, person = PersonDTO
                    .fromDomain(this.person)
            )
        }
    }
}