package org.fmm.communitymgmt.ui.home.comlist.selectcommunity.recyclerview

import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.databinding.ItemCommunitySelectBinding
import org.fmm.communitymgmt.domainmodels.model.CommunityModel

class CommunitySelectViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemCommunitySelectBinding.bind(view)

    fun render(community: CommunityModel, onItemSelected: (CommunityModel) -> Unit){
        val connector = when (community.communityNumber.takeLast(1)) {
            "1" -> "st"
            "2" -> "nd"
            "3" -> "rd"
            else -> "th"
        }

        val aux =             binding.root.context.getString(
            R.string.communityName,
            community.communityNumber,
            connector,
            community.parish
        )
        val numberDigits = community.communityNumber.length
        val connectorCharacters = connector.length
        val mStringSpan = SpannableString(aux)
        mStringSpan.setSpan(SuperscriptSpan(),numberDigits, numberDigits+connectorCharacters, Spannable
            .SPAN_EXCLUSIVE_EXCLUSIVE)
        mStringSpan.setSpan(RelativeSizeSpan(0.75f),numberDigits, numberDigits+connectorCharacters, Spannable
            .SPAN_EXCLUSIVE_EXCLUSIVE)


//        Toast.makeText(this.itemView.context, aux, Toast.LENGTH_LONG).show()
        binding.communityName.text = mStringSpan
        binding.communityName.setOnClickListener {
            onItemSelected(community)
        }
//        binding.cvCommunityItem.setOnClickListener {
//            itemClicked(newLambda = {onItemSelected(community)})
//        }
    }

    private fun itemClicked(newLambda: () -> Unit) {
        newLambda()
    }
}