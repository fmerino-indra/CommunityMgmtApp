package org.fmm.communitymgmt.ui.home.comlist.selectcommunity.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.domainmodels.model.CommunityModel
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import org.fmm.communitymgmt.ui.security.model.UserSession

class CommunitySelectAdapter(
    private var communities: List<CommunityModel>,
    private val onItemSelected: (CommunityModel) -> Unit
):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CommunitySelectViewHolder(LayoutInflater.from(parent.context).inflate(R.layout
            .item_community_select,parent, false))
    }

    override fun getItemCount(): Int {
        return communities.size ?:0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = communities[position]
        (holder as CommunitySelectViewHolder).render(item,onItemSelected)
    }

    fun updateCommunities(list:List<CommunityModel>) {
        communities = list
        notifyDataSetChanged()
    }
}