package org.fmm.communitymgmt.ui.home.comlist.list.recyclerview

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.databinding.ItemCommunitylistMarriageBinding
import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship
import org.fmm.communitymgmt.domainmodels.model.EmailAccount
import org.fmm.communitymgmt.domainmodels.model.MarriageModel
import java.util.stream.Collectors
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.math.log

class CommunityListMarriageViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemCommunitylistMarriageBinding.bind(view)

    fun render(relationship: AbstractRelationship, onItemSelected: (AbstractRelationship) -> Unit) {
        Log.d("[FMMP]", "Tiene que pintar esto: ${relationship.relationshipName}")
        when(relationship) {
            is MarriageModel -> {
                var bitmap = decodeBase64ToBitmap((relationship.husband.image))
                if (bitmap != null) {
                    binding.hPhoto.setImageBitmap(bitmap)
                } else {
                    binding.hPhoto.setImageResource(R.drawable.ic_male_face)
                }
                bitmap = decodeBase64ToBitmap((relationship.wife.image))
                if (bitmap != null) {
                    binding.wPhoto.setImageBitmap(bitmap)
                } else {
                    binding.wPhoto.setImageResource(R.drawable.ic_female_face)
                }
                binding.hNickName.text = relationship.husband.nickname
                binding.husbandName.text = binding.root.context.getString(R.string.marriageName,
                    relationship.husband.name, relationship.husband.surname1, relationship
                        .husband.surname2)

                binding.hPhone.text = relationship.husband.mobileNumbers?.joinToString(
                    separator = ", "){
                    it.mobileNumber
                }
                Log.d("[FMMP]", "Nº de teléfono ${relationship.relationshipName} tratados")
                binding.hEmail.text = relationship.husband.emailAccounts?.joinToString(
                    separator = ", "){
                    it.emailAccount
                }
                /*
                Hay una función que hace esto:
                    lista.jointToString(separator = ", ")
                 */
                binding.wNickName.text = relationship.wife.nickname
                binding.wifeName.text = binding.root.context.getString(R.string.marriageName,
                    relationship.wife.name, relationship.wife.surname1, relationship.wife.surname2)

                binding.wPhone.text = relationship.wife.mobileNumbers?.joinToString(
                    separator = ", "){
                    it.mobileNumber
                }
                binding.wEmail.text = relationship.wife.emailAccounts?.joinToString (
                    separator = ", "){
                    it.emailAccount
                }
            }
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun decodeBase64ToBitmap(base64String: String?): Bitmap? {
        return try {
            if (base64String.isNullOrBlank()) return null
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            null
        }
    }

}