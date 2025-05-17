package org.fmm.communitymgmt.util

import kotlinx.datetime.LocalDate

fun Long.toLocalDate(): LocalDate = LocalDate.fromEpochDays(this.toInt())
fun LocalDate.toEpochDaysLong(): Long {
    return this.toEpochDays().toLong()
}

fun LocalDate.formatSpanish(): String {
    val day = this.dayOfMonth.toString().padStart(2, '0')
    val month = this.monthNumber.toString().padStart(2, '0')
    val year = this.year.toString()
    return "$day/$month/$year"
}

class DateExtensions {
    companion object {
        fun parseSpanishDate(str: String?): LocalDate? {
            if (!str.isNullOrBlank()) {
                val parts = str.split("/")
                require(parts.size == 3) { "Invalid date format" }
                val day = parts[0].toInt()
                val month = parts[1].toInt()
                val year = parts[2].toInt()
                /*
                val fecha = LocalDate(dayOfMonth = day, monthNumber =  month, year =  year)
                try {
                    val dias = fecha.toEpochDaysLong()
                    Log.d("DateExtensions", "La fecha en días: ${dias}")
                } catch (e: Exception) {
                    Log.e("DateExtensions", "Un error convirtiendo a días", e)
                }

                 */
                return LocalDate(dayOfMonth = day, monthNumber = month, year = year)
            } else return null
        }
    }
}