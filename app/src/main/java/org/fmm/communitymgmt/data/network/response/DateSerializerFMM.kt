package org.fmm.communitymgmt.data.network.response

import android.icu.text.SimpleDateFormat
import android.text.format.DateFormat
import kotlinx.datetime.LocalDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.Locale

//@Serializer(forClass = LocalDate::class)
object DateSerializerFMM:KSerializer<LocalDate> {
    private val df = SimpleDateFormat("yyyy/MM/dd")

    override val descriptor: SerialDescriptor
        get() = TODO("Not yet implemented")

    override fun deserialize(decoder: Decoder): LocalDate {
        TODO("Not yet implemented")
    }

    override fun serialize(encoder: Encoder, value: LocalDate) {
        TODO("Not yet implemented")
    }


}