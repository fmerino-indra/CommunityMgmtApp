package org.fmm.communitymgmt.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.fmm.communitymgmt.domainmodels.model.PersonModel
import java.util.stream.Collectors

@Serializable
@SerialName("Other")
data class OtherDTO(
    override val id: Int,
    override val relationshipName: String,
    val relatedPersons: List<PersonDTO>

    ) : AbstractRelationshipDTO() {


        fun toDomain():List<PersonModel> {
            val personList  =
                relatedPersons.stream().map { it.toDomain() }.collect(Collectors.toList())
            return personList

        }
}