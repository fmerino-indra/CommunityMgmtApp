package org.fmm.communitymgmt.domainlogic.usecase

import org.fmm.communitymgmt.domainlogic.repositories.RelationshipRepository
import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship
import javax.inject.Inject

class GetRelationship @Inject constructor(private val repository:RelationshipRepository){
    suspend operator fun invoke(id:Int) = repository.getRelationshipDetail(id)
}