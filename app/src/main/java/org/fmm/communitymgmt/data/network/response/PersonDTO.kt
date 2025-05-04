package org.fmm.communitymgmt.data.network.response

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.serializers.LocalDateComponentSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import org.fmm.communitymgmt.domainmodels.model.Genders
import org.fmm.communitymgmt.domainmodels.model.PersonModel
import java.util.stream.Collectors

@Serializable
data class PersonDTO(
    val id: Int?=null,
    val name: String,
    val surname1: String?,
    val surname2: String? = null,
    val nickname: String,
    val mobileNumbers: List<MobileNumberDTO>? = null,
    val emailAccounts: List<EmailAccountDTO>? = null,

    val birthday: Long?,
    val gender: String,
    val image: ImageDTO? = null
) {

    fun toDomain(): PersonModel =
        PersonModel(
            id, name, surname1, surname2, nickname,
            mobileNumbers?.stream()?.map {
                it.toDomain()
            }?.collect(Collectors.toList()),
            emailAccounts?.stream()?.map {
                it.toDomain()
            }?.collect(Collectors.toList()),
            birthday,
            if ("M" == gender) Genders.MALE else Genders.FEMALE,
            image?.tinyPhoto
        )
    companion object {
        fun fromDomain(person: PersonModel) = person.run {
            PersonDTO(id = id,
                name = name,
                surname1 = surname1,
                surname2 = surname2,
                nickname = nickname,
                mobileNumbers = person.mobileNumbers?.stream()
                    ?.map { MobileNumberDTO.fromDomain(it) }
                    ?.collect(Collectors.toList()),
                emailAccounts = person.emailAccounts?.stream()
                    ?.map { EmailAccountDTO.fromDomain(it) }
                    ?.collect(Collectors.toList()),
                birthday,
                gender.genderChar.toString(),
                null)
        }
    }
}