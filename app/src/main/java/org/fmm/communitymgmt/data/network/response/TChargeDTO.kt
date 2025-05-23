package org.fmm.communitymgmt.data.network.response

import kotlinx.serialization.Serializable
import org.fmm.communitymgmt.domainmodels.model.TChargeModel

@Serializable
data class TChargeDTO (
    val id:Int,
    val charge:String
) {
    fun toDomain(): TChargeModel =
        if (id == 1) TChargeModel.Responsible
        else TChargeModel.ResponsibleSpouse
// @TODO Revisar los posibles valores

    companion object {
        fun fromDomain(charge: TChargeModel) =
            when(charge) {
                TChargeModel.Responsible -> TChargeDTO(TChargeModel.Responsible.id, TChargeModel
                    .Responsible.charge)
                TChargeModel.ResponsibleSpouse -> TChargeDTO(TChargeModel.ResponsibleSpouse.id,
                    TChargeModel.ResponsibleSpouse.charge)
            }
    }
}