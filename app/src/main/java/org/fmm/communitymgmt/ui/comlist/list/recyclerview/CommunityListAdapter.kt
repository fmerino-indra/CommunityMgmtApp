package org.fmm.communitymgmt.ui.comlist.list.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AutoCompleteTextView.Validator
import androidx.recyclerview.widget.RecyclerView
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship
import org.fmm.communitymgmt.domainmodels.model.MarriageModel
import org.fmm.communitymgmt.domainmodels.model.OtherModel
import org.fmm.communitymgmt.domainmodels.model.SingleModel

class CommunityListAdapter(private var communityList: List<AbstractRelationship> = emptyList(),
                           private val onItemSelected: (AbstractRelationship) ->
                           Unit):
    RecyclerView
    .Adapter<RecyclerView.ViewHolder>() {
        companion object {
            private const val VIEW_TYPE_MARRIAGE=0
            private const val VIEW_TYPE_SINGLE=1
            private const val VIEW_TYPE_OTHER=2
        }

    override fun getItemViewType(position: Int): Int {
        return when (communityList[position]) {
            is MarriageModel -> VIEW_TYPE_MARRIAGE
            is SingleModel -> VIEW_TYPE_SINGLE
            is OtherModel -> VIEW_TYPE_OTHER

            else -> VIEW_TYPE_SINGLE
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            VIEW_TYPE_MARRIAGE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout
                    .item_communitylist_marriage, parent, false)
                CommunityListMarriageViewHolder(view)
            }
            VIEW_TYPE_SINGLE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout
                    .item_communitylist_single, parent, false)
                CommunityListSingleViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout
                    .item_communitylist_marriage, parent, false)
                CommunityListMarriageViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return communityList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = communityList[position]) {
            is MarriageModel -> (holder as CommunityListMarriageViewHolder).render(item,
                onItemSelected)
            is SingleModel -> (holder as CommunityListSingleViewHolder).render(item,
                onItemSelected)
        }
    }

    fun updateCommunityList(list:List<AbstractRelationship>) {
        communityList = list
        notifyDataSetChanged()
    }
}