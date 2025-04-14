package org.fmm.communitymgmt.data.network.response

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship

typealias CommunityListDTO = List<@Polymorphic AbstractRelationshipDTO>

@Serializable
@Polymorphic
abstract class AbstractRelationshipDTO {
    abstract val id: Int
    abstract val relationshipName: String
    //abstract fun toDomain(): AbstractRelationship

}