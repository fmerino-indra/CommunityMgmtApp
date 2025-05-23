package org.fmm.communitymgmt.data.network.response

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship
import org.fmm.communitymgmt.domainmodels.model.MarriageModel
import org.fmm.communitymgmt.domainmodels.model.SingleModel

typealias CommunityListDTO = List<@Polymorphic AbstractRelationshipDTO>

@Serializable
@Polymorphic
abstract class AbstractRelationshipDTO {
    abstract val id: Int
    abstract val relationshipName: String
    abstract fun toDomain(): AbstractRelationship

    companion object RelationshipDTOFactory {
        fun fromDomain(relationship: AbstractRelationship): AbstractRelationshipDTO =
            when (relationship) {
                is MarriageModel -> MarriageDTO.fromDomain(relationship)
                is SingleModel -> SingleDTO.fromDomain(relationship)
                else -> error("Hay que quitar el Other")
            }
    }
}