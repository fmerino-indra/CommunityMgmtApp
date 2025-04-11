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

    override fun toDomain():SingleModel = SingleModel(id, relationshipName, person.toDomain())
}