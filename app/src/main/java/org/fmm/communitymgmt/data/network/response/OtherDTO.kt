package org.fmm.communitymgmt.data.network.response

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.fmm.communitymgmt.domainmodels.model.OtherModel
import org.fmm.communitymgmt.domainmodels.model.PersonModel
import java.util.stream.Collectors
import kotlin.streams.toList

@Serializable
@SerialName("Other")
data class OtherDTO(
    override val id: Int,
    override val relationshipName: String,
    val relatedPersons: List<PersonDTO>

    ) : AbstractRelationshipDTO() {


        override fun toDomain():OtherModel {
            val personList  =
                relatedPersons.stream().map { it.toDomain() }.collect(Collectors.toList())
            return OtherModel(id, relationshipName, personList)

        }
}