package org.fmm.communitymgmt.domainmodels.model

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

/*
En lugar de hacerlo así, definiendo el constructor, se elimina la definición del constructor y
sólo tiene estructura
 */
//abstract class Relationship(val id: Int, val relationshipName: String)

abstract class AbstractRelationship {
    abstract val id: Int
    abstract val relationshipName: String
}
