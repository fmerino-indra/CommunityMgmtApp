package org.fmm.communitymgmt.data.network.response

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.fmm.communitymgmt.domainmodels.model.Genders
import org.fmm.communitymgmt.domainmodels.model.PersonModel
import java.time.LocalDate

@Serializable
@SerialName("Marriage")
data class PersonDTO(
    val name: String,
    val surname1: String?,
    val surname2: String? = null,
    val nickname: String,
    val emailAccount: String? = null,
//    val birthday: LocalDate,
    val gender: String,
    val image: ImageDTO? = null
    ) {
    fun toDomain():PersonModel = PersonModel(name, surname1, surname2, nickname, emailAccount,
        LocalDate.parse("2025-01-01"), Genders.MALE, image?.smallPhoto)
}