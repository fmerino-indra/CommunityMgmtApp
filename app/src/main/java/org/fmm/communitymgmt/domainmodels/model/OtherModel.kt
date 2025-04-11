package org.fmm.communitymgmt.domainmodels.model

data class OtherModel(
    override val id: Int,
    override val relationshipName: String,
    val relatedPersons: List<PersonModel>

    ) : AbstractRelationship() {

}