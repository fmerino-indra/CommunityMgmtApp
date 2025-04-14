package org.fmm.communitymgmt.data.network.response

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.fmm.communitymgmt.domainmodels.model.Genders
import org.fmm.communitymgmt.domainmodels.model.PersonModel
import java.time.LocalDate
import java.util.stream.Collectors

@Serializable
@SerialName("Marriage")
data class PersonDTO(
    val name: String,
    val surname1: String?,
    val surname2: String? = null,
    val nickname: String,
    val mobileNumbers: List<MobileNumberDTO>? = null,
    val emailAccounts: List<EmailAccountDTO>? = null,
//    val birthday: LocalDate,
    val gender: String,
    val image: ImageDTO? = null
) {
//    fun toDomain():PersonModel = PersonModel(name, surname1, surname2, nickname,mobileNumbers,
//            emailAccounts, LocalDate.parse("2025-01-01"), if ("M"==gender) Genders.MALE else
//    Genders.FEMALE, image?.tinyPhoto)

    fun toDomain(): PersonModel =
        PersonModel(
            name, surname1, surname2, nickname,
            mobileNumbers?.stream()?.map {
                it.toDomain()
            }?.collect(Collectors.toList()),
            emailAccounts?.stream()?.map {
                it.toDomain()
            }?.collect(Collectors.toList()),
            LocalDate.parse("2025-01-01"),
            if ("M" == gender) Genders.MALE else Genders.FEMALE,
            image?.tinyPhoto
        )
}