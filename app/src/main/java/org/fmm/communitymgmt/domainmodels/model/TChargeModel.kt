package org.fmm.communitymgmt.domainmodels.model

sealed class TChargeModel (
    val id:Int,
    val charge:String
) {
    data object Responsible: TChargeModel(1, "Responsible")
    data object ResponsibleSpouse: TChargeModel(2,"Responsible spouse")
    // Terminar todos los tipos
}