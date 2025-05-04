package org.fmm.communitymgmt.domainmodels.model

import android.os.Parcelable
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.parcelize.Parcelize
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.util.Locale

@Parcelize
data class PersonModel(
    val id: Int?,
    val name: String,
    val surname1: String?,
    val surname2: String? = null,
    val nickname: String,
    val mobileNumbers: List<MobileNumber>?=null,
    val emailAccounts: List<EmailAccount>? = null,
    val birthday: Long?,
    val gender: Genders,
    val image: String? = null
    ): Parcelable {
}