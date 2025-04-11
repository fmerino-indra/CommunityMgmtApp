package org.fmm.communitymgmt.domainmodels.model

data class SingleModel(
    override val id: Int,
    override val relationshipName: String,
    val person: PersonModel,

    ) : AbstractRelationship() {

}