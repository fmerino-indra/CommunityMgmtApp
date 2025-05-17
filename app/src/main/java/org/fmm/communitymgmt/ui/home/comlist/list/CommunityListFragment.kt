package org.fmm.communitymgmt.ui.home.comlist.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.databinding.FragmentCommunityListBinding
import org.fmm.communitymgmt.domainmodels.model.CommunityModel
import org.fmm.communitymgmt.ui.home.comlist.list.recyclerview.CommunityListAdapter
import org.fmm.communitymgmt.ui.home.comlist.selectcommunity.CommunitySelectFragment

@AndroidEntryPoint
class CommunityListFragment : Fragment() {
    private val communityListViewModel by viewModels<CommunityListViewModel>()

    private var _binding: FragmentCommunityListBinding? = null
    private val binding get() = _binding!!

    private lateinit var communityListAdapter: CommunityListAdapter

    private lateinit var communitySelectFragment: CommunitySelectFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initUI()
    }

    // Hacemos la carga de los datos pidiéndoselo al viewModel. Podría hacerlo él mismo desde init{}
    private fun initData() {
        if (communityListViewModel.userSession.userInfo?.allCommunities?.size == 1)
            communityListViewModel.selectCommunity(communityListViewModel.userSession.userInfo!!.allCommunities!![0])
        else
            communityListViewModel.getData()
    }

    private fun initUI() {
        initAdapter()
        initDialog()
        initUIState()
        initToolbar()
    }

    private fun initDialog() {
        communitySelectFragment=CommunitySelectFragment(onSelected = {
            communityListViewModel.selectCommunity(it)
        })
    }

    private fun initToolbar() {

    }

    private fun initAdapter() {
        // De momento no le pasamos función para onSelect
        communityListAdapter = CommunityListAdapter(onItemSelected = {
            Log.d("[FMMP]", "Se ha pulsado sobre el CardView ${it.relationshipName}")

            findNavController().navigate(CommunityListFragmentDirections
                .actionCommunityListFragmentToEditPersonFragment(it.id))
        })
        val lM = GridLayoutManager(context, 2)
        lM.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = communityListAdapter.getItemViewType(position)
                return if (viewType == 0) {
                    2
                } else {
                    1
                }
            }
        }
//        binding.rvCommunityList.apply {
//            layoutManager = GridLayoutManager(context, 2)
//            layoutManager = lM
//            adapter = communityListAdapter
//        }


        binding.rvCommunityList.apply {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = communityListAdapter
        }
    }

    private fun initUIState() {
        // Esta se engancha al ciclo de vida del fragmento
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                communityListViewModel.state.collect{
                    when(it) {
                        is CommunityListState.Error -> errorState(it)
                        CommunityListState.Loading -> loadingState()
                        is CommunityListState.Success -> successState(it)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                communityListViewModel.selectedCommunity.collect {
                    if (it is CommunitySelectState.NotSelected) {
                        notSelectedState()
                        // Quitar panel semitransparente cuando lo haya
                    } else
                        selectedState()
                }
            }
        }
    }

    private fun errorState(communityListState: CommunityListState.Error) {
        binding.progressBar.isVisible = false
        Log.d("[FMMP]", "Se ha producido un error al recibir o pintar la información")
        Toast.makeText(
            requireContext(), getString(R.string.listException, communityListState.error), Toast
                .LENGTH_LONG
        ).show()
    }

    private fun loadingState() {
        binding.progressBar.isVisible = true
    }

    private fun successState(state: CommunityListState.Success) {
        binding.progressBar.isVisible = false
/*
        if (state.userInfo.imageUrl.isNotBlank() && state.userInfo.imageUrl.isNotEmpty()) {
            val request = ImageRequest.Builder(requireContext())
                .data(state.userInfo.imageUrl)
                .target(binding.userInfoPhoto)
                .build()

            iLoader.enqueue(request)
        }
*/

        communityListAdapter.updateCommunityList(state.communityList)
    }

    private fun notSelectedState() {
        binding.loadingOverlay.isVisible = true
        communitySelectFragment.show(parentFragmentManager, "select_sheet")
    }

    private fun selectedState() {
        binding.loadingOverlay.isVisible = false
        if (communitySelectFragment.isVisible)
            communitySelectFragment.dismiss()
        communityListViewModel.getData()
    }
}