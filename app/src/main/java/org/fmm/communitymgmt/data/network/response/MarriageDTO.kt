package org.fmm.communitymgmt.data.network.response

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

    override fun toDomain():MarriageModel = MarriageModel(id, relationshipName, husband.toDomain(),
        wife.toDomain(), LocalDate.of(2000,12,12))

    companion object {
        fun fromDomain(model: MarriageModel) = model.run {
            MarriageDTO(
                id = id,
                relationshipName,
                PersonDTO.fromDomain(husband),
                PersonDTO.fromDomain(wife)
            )
        }
    }
}