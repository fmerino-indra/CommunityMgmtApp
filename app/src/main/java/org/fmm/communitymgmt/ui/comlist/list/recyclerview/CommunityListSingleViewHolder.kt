package org.fmm.communitymgmt.ui.comlist.list.recyclerview

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.databinding.ItemCommunitylistMarriageBinding
import org.fmm.communitymgmt.databinding.ItemCommunitylistSingleBinding
import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship
import org.fmm.communitymgmt.domainmodels.model.Genders
import org.fmm.communitymgmt.domainmodels.model.MarriageModel
import org.fmm.communitymgmt.domainmodels.model.SingleModel
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.math.log

class CommunityListSingleViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemCommunitylistSingleBinding.bind(view)

    fun render(relationship: AbstractRelationship, onItemSelected: (AbstractRelationship) -> Unit){
        Log.d("[FMMP]", "Tiene que pintar esto: ${relationship.relationshipName}")
        when(relationship) {
            is SingleModel -> {
                var bitmap = decodeBase64ToBitmap((relationship.person.image))
                if (bitmap != null) {
                    binding.photo.setImageBitmap(bitmap)
                } else {
                    if (relationship.person.gender == Genders.MALE) {
                        binding.photo.setImageResource(R.drawable.ic_male_face)
                    } else {
                        binding.photo.setImageResource(R.drawable.ic_female_face)
                    }
                }
                binding.fullName.text = binding.root.context.getString(R.string.marriageName,
                    relationship.person.name, relationship.person.surname1, relationship.person.surname2)

                binding.nickName.text = relationship.person.nickname
                binding.phone.text = relationship.person.mobileNumbers?.joinToString (
                    separator = ", "){
                    it.mobileNumber
                }
                binding.email.text = relationship.person.emailAccounts?.joinToString (
                    separator = ", "){
                    it.emailAccount
                }
                binding.cvCommunityItem.setOnClickListener {
                    itemClicked(newLambda = {onItemSelected(relationship)})
                }
            }
        }
    }

    private fun itemClicked(newLambda: () -> Unit) {
        newLambda()
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