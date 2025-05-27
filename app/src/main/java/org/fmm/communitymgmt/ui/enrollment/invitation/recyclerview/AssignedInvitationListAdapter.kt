package org.fmm.communitymgmt.ui.enrollment.invitation.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.domainmodels.model.FullInvitationModel

class AssignedInvitationListAdapter(
    private var invitationList: List<FullInvitationModel> =
        emptyList(),
    private val onItemSelectedModel: (FullInvitationModel) -> Unit,
    private val onItemDeselectedModel: ()->Unit
) :
    RecyclerView.Adapter<AssignedInvitationListViewHolder>() {

    private var currentSelectedIndex: Int = -1

    fun getCurrentSelectedIndex():Int {
        return currentSelectedIndex
    }
    fun setCurrentSelectedIndex(selected: Int) {
        // Si el valor es -1 es que el seleccionado era el mismo, no hace falta recorrer el array
        val antSelected = currentSelectedIndex
        currentSelectedIndex = selected
        if (antSelected > -1 && selected > -1) { // and selected >-1, porque si es -1 está
            // deseleccionando
            notifyItemChanged(antSelected)
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AssignedInvitationListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout
                .item_assigned_invitation, parent, false
        )
        return AssignedInvitationListViewHolder(view)
    }

    override fun getItemCount() = invitationList.size

    override fun onBindViewHolder(holder: AssignedInvitationListViewHolder, position: Int) {
        val item = invitationList[position]
        holder.render(item, position,
            getSelectedIndex = {getCurrentSelectedIndex()},
            setSelectedLambda = {
                if (getCurrentSelectedIndex() == position) {
                    setCurrentSelectedIndex(-1)
                } else {
                    setCurrentSelectedIndex(position)
                }
            },
            onItemSelectedModelLambda=
            {
                if (getCurrentSelectedIndex() > -1)
                    onItemSelectedModel(item)
                else
                    onItemDeselectedModel()
            }
        )
    }

    fun updateInvitationList(list: List<FullInvitationModel>) {
        invitationList = list
        notifyDataSetChanged()
    }
}