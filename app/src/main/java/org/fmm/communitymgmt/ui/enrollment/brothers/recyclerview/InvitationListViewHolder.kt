package org.fmm.communitymgmt.ui.enrollment.brothers.recyclerview

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.databinding.ItemCommunitylistSingleBinding
import org.fmm.communitymgmt.databinding.ItemInvitationBinding
import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship
import org.fmm.communitymgmt.domainmodels.model.Genders
import org.fmm.communitymgmt.domainmodels.model.InvitationModel
import org.fmm.communitymgmt.domainmodels.model.SingleModel
import kotlin.io.encoding.ExperimentalEncodingApi

class InvitationListViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemInvitationBinding.bind(view)

    fun render(invitation: InvitationModel, onItemSelected: (InvitationModel) -> Unit){
        // Hay que generar el QR
        binding.invitationName.text = invitation.name
        if (invitation.iat !=null)
            binding.qrDate.text = Instant.fromEpochMilliseconds(invitation.iat).toLocalDateTime(TimeZone.of("Europe/Madrid")).toString()
        if (invitation.exp !=null)
            binding.qrExp.text = Instant.fromEpochMilliseconds(invitation.exp).toLocalDateTime(TimeZone.of("Europe/Madrid")).toString()
        binding.cvInvitation.setOnClickListener {
            itemClicked(newLambda = {onItemSelected(invitation)})
        }
    }

    private fun itemClicked(newLambda: () -> Unit) {
        newLambda()
    }

}