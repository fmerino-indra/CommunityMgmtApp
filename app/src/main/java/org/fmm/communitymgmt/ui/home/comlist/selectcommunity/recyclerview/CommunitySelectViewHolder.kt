package org.fmm.communitymgmt.ui.home.comlist.selectcommunity.recyclerview

import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.databinding.ItemCommunitySelectBinding
import org.fmm.communitymgmt.domainmodels.model.CommunityInfoModel
import org.fmm.communitymgmt.domainmodels.model.CommunityModel

class CommunitySelectViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemCommunitySelectBinding.bind(view)

    fun render(community: CommunityInfoModel, position: Int, size:Int,
               onItemSelected:(CommunityInfoModel)-> Unit)
    {
        val connector = when (community.myCommunityData.communityNumber.takeLast(1)) {
            "1" -> "st"
            "2" -> "nd"
            "3" -> "rd"
            else -> "th"
        }

        val aux = community.myCommunityData.let {
            binding.root.context.getString(
                R.string.communityName,
                it.communityNumber,
                connector,
                it.parish
            ) }

        val numberDigits = community.myCommunityData.communityNumber.length
        val connectorCharacters = connector.length
        val mStringSpan = SpannableString(aux)
        mStringSpan.setSpan(SuperscriptSpan(),numberDigits, numberDigits+connectorCharacters, Spannable
            .SPAN_EXCLUSIVE_EXCLUSIVE)
        mStringSpan.setSpan(RelativeSizeSpan(0.75f),numberDigits, numberDigits+connectorCharacters, Spannable
            .SPAN_EXCLUSIVE_EXCLUSIVE)

        if (position==0)
            binding.communityName.background=AppCompatResources.getDrawable(
                binding.root.context,
                R.drawable.rounded_corner_top_view
            )
        else if (position==size-1)
            binding.communityName.background=AppCompatResources.getDrawable(
                binding.root.context,
                R.drawable.rounded_corner_bottom_view
            )

//        Toast.makeText(this.itemView.context, aux, Toast.LENGTH_LONG).show()
        binding.communityName.text = mStringSpan
        binding.communityName.setOnClickListener {
            onItemSelected(community)
        }
    }
}