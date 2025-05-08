package org.fmm.communitymgmt.ui.enrollment.brothers.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship
import org.fmm.communitymgmt.domainmodels.model.InvitationModel
import org.fmm.communitymgmt.domainmodels.model.MarriageModel
import org.fmm.communitymgmt.domainmodels.model.SingleModel

class InvitationListAdapter(private var invitationList: List<InvitationModel> = emptyList(),
                            private val onItemSelected: (InvitationModel) -> Unit):
    RecyclerView.Adapter<InvitationListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvitationListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout
            .item_invitation, parent, false)
        return InvitationListViewHolder(view)
    }

    override fun getItemCount() = invitationList.size

    override fun onBindViewHolder(holder: InvitationListViewHolder, position: Int) {
        val item = invitationList[position]
        holder.render(item,onItemSelected)
    }

    fun updateInvitationList(list:List<InvitationModel>) {
        invitationList = list
        notifyDataSetChanged()
    }
}