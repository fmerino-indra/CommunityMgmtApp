package org.fmm.communitymgmt.ui.home.comlist.selectcommunity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.fmm.communitymgmt.databinding.FragmentCommunitySelectBinding
import org.fmm.communitymgmt.domainmodels.model.CommunityInfoModel
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import org.fmm.communitymgmt.ui.common.UserInfoState
import org.fmm.communitymgmt.ui.common.UserInfoViewModel
import org.fmm.communitymgmt.ui.home.comlist.selectcommunity.recyclerview.CommunitySelectAdapter

@AndroidEntryPoint
class CommunitySelectFragment(
    private val onSelected: (selected: CommunityInfoModel) -> Unit

) : BottomSheetDialogFragment() {
    private var _binding: FragmentCommunitySelectBinding? = null
    private val binding get() = _binding!!

    private val userInfoViewModel:UserInfoViewModel by activityViewModels<UserInfoViewModel>()
    private lateinit var userInfo: UserInfoModel

    private lateinit var communitySelectAdapter: CommunitySelectAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunitySelectBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initUI()
    }

    private fun initData() {
        // FMMP por aquí
        userInfo = userInfoViewModel.userSession.userInfo
        Log.d("CommunitySelectFragment", userInfo.selectedCommunity?.myCommunityData?.parish ?: ("NO HAY " +
                "COMUNIDAD")
        )
        // Nothing to do
    }

    private fun initUI() {
        initAdapter()
        initNavigation()
//        initListeners()
    }

    private fun initAdapter() {
        communitySelectAdapter = CommunitySelectAdapter(userInfo.myCommunities ?: emptyList(),
            onItemSelected = {
                selectCommunity(it)
            })
        binding.rvCommunities.apply {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = communitySelectAdapter
        }
    }
    private fun initNavigation() {
    }

    private fun selectCommunity(community:CommunityInfoModel) {
        onSelected(community)
    }
}