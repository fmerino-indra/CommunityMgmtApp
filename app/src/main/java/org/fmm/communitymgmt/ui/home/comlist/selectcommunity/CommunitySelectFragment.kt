package org.fmm.communitymgmt.ui.home.comlist.selectcommunity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.databinding.FragmentCommunitySelectBinding
import org.fmm.communitymgmt.domainmodels.model.CommunityModel
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import org.fmm.communitymgmt.ui.common.UserInfoViewModel
import org.fmm.communitymgmt.ui.home.comlist.selectcommunity.recyclerview.CommunitySelectAdapter

@AndroidEntryPoint
class CommunitySelectFragment(
    private val onSelected: (selected: CommunityModel) -> Unit

) : BottomSheetDialogFragment() {
    private var _binding: FragmentCommunitySelectBinding? = null
    private val binding get() = _binding!!

    private val userInfoViewModel:UserInfoViewModel by viewModels<UserInfoViewModel>()
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
        userInfo = userInfoViewModel.userInfo
        Log.d("CommunitySelectFragment", userInfo.community?.parish ?: "NO HAY COMUNIDAD")
        // Nothing to do
    }

    private fun initUI() {
        initAdapter()
        initNavigation()
//        initListeners()
    }

    private fun initAdapter() {
        communitySelectAdapter = CommunitySelectAdapter(userInfo.allCommunities ?: emptyList(),
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

    private fun selectCommunity(community:CommunityModel) {
        onSelected(community)
    }
}