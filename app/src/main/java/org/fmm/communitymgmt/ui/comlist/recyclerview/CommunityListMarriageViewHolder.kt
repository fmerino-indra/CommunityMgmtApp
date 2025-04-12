package org.fmm.communitymgmt.ui.comlist.recyclerview

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.databinding.ItemCommunitylistMarriageBinding
import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship
import org.fmm.communitymgmt.domainmodels.model.MarriageModel
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.math.log

class CommunityListMarriageViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemCommunitylistMarriageBinding.bind(view)

    fun render(relationship: AbstractRelationship){
        Log.d("[FMMP]", "Tiene que pintar esto: ${relationship.relationshipName}")
        when(relationship) {
            is MarriageModel -> {
                var bitmap = decodeBase64ToBitmap((relationship.husband.image))
                if (bitmap != null) {
                    binding.husbandPhoto.setImageBitmap(bitmap)
                } else {
                    binding.husbandPhoto.setImageResource(R.drawable.ic_male_face)
                }
                bitmap = decodeBase64ToBitmap((relationship.wife.image))
                if (bitmap != null) {
                    binding.wifePhoto.setImageBitmap(bitmap)
                } else {
                    binding.wifePhoto.setImageResource(R.drawable.ic_female_face)
                }

                binding.husbandName.text = relationship.husband.nickname
                binding.husbandPhone.text = relationship.husband.emailAccount

                binding.wifeName.text = relationship.wife.nickname
                binding.wifePhone.text = relationship.wife.emailAccount
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