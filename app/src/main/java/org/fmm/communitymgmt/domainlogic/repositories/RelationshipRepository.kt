package org.fmm.communitymgmt.domainlogic.repositories

import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship

interface RelationshipRepository {
    suspend fun getRelationshipDetail(id:Int): AbstractRelationship?
}