package org.fmm.communitymgmt.data.network.response

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.fmm.communitymgmt.domainmodels.model.MarriageModel
import java.time.LocalDate

@Serializable
@SerialName("Marriage")
data class MarriageDTO(
    override val id: Int,
    override val relationshipName: String,
    val husband: PersonDTO,
    val wife: PersonDTO,
//    val weddingDate: LocalDate

    ) : AbstractRelationshipDTO() {

    fun toDomain():MarriageModel = MarriageModel(id, relationshipName, husband.toDomain(),
        wife.toDomain(), LocalDate.of(2000,12,12))
}