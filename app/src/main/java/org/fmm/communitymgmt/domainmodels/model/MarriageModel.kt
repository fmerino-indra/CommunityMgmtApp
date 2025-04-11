package org.fmm.communitymgmt.domainmodels.model

import java.time.LocalDate

data class MarriageModel(
    override val id: Int,
    override val relationshipName: String,
    val husband: PersonModel,
    val wife: PersonModel,
    val weddingDate: LocalDate

    ) : AbstractRelationship() {

}