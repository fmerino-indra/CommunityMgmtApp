package org.fmm.communitymgmt.ui.comlist.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.ImageLoader
import coil.request.ImageRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.fmm.communitymgmt.databinding.FragmentCommunityListBinding
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import org.fmm.communitymgmt.ui.comlist.list.recyclerview.CommunityListAdapter
import org.fmm.communitymgmt.ui.security.model.UserSession
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [CommunityListFragment.newInstance] factory method to
 * create an instance of this fragment.
 * @AndroidEntryPoint Per
 * mite cosas inyectadas
 */
@AndroidEntryPoint
class CommunityListFragment : Fragment() {
    private val communityListViewModel by viewModels<CommunityListViewModel>()

    private var _binding: FragmentCommunityListBinding? = null
    private val binding get() = _binding!!

    private lateinit var communityListAdapter: CommunityListAdapter

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
        communityListViewModel.getData()
    }

    private fun initUI() {
        initAdapter()
        initUIState()
        initToolbar()
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
                        is CommunityListState.Error -> errorState()
                        CommunityListState.Loading -> loadingState()
                        is CommunityListState.Success -> successState(it)
                    }
                }
            }
        }
    }

    private fun errorState() {
        binding.progressBar.isVisible = false
        Log.d("[FMMP]", "Se ha producido un error al recibir o pintar la información")
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
        Log.i("[FMMP]", "Debería estar pintando el RecyclerView")
        Log.i("[FMMP", "Tamaño: ${state.communityList.size}")
    }

}