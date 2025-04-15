package org.fmm.communitymgmt.ui.comlist.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.databinding.FragmentEditPersonBinding
import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship
import org.fmm.communitymgmt.domainmodels.model.SingleModel

@AndroidEntryPoint
class EditPersonFragment : Fragment() {
    private val editPersonViewModel by viewModels<EditPersonViewModel>()

    private lateinit var _binding: FragmentEditPersonBinding
    private val binding get() = _binding

    private val args: EditPersonFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentEditPersonBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initUI()
    }

    private fun initData() {
        editPersonViewModel.getData(args.relationshipId)
    }

    private fun initUI() {
        initToolbar()
        initListeners()
        initUIState()
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initListeners() {
        binding.btnAccept.setOnClickListener {
            onAccept()
        }

        binding.btnCancel.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun onAccept() {
        TODO("Not yet implemented")
    }

    /**
     * Como tiene ViewModel y StateFlow, este método configura el collect del state
     * Lo asocia a un estado del ciclo de vida y lo mete en una corrutina
     */
    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                editPersonViewModel.state.collect {
                    when (it) {
                        EditPersonState.Loading -> loadingState()
                        is EditPersonState.ViewMode -> viewingState(it)
                        is EditPersonState.EditMode -> editingState(it)
                        is EditPersonState.Error -> errorState()
                    }
                }
            }
        }
    }

    private fun loadingState() {
        binding.progressBar.isVisible = true
    }
    private fun viewingState(it: EditPersonState.ViewMode) {
        binding.progressBar.isVisible = false
        binding.btnAccept.isVisible = false
        binding.btnCancel.setText("Back")
        if (it.relationship is SingleModel) {
            binding.name.setText(it.relationship.person.name)
            binding.surname1.setText(it.relationship.person.surname1)
            binding.surname2.setText(it.relationship.person.surname2)
        }

        binding.name.isEnabled = false
        binding.surname1.isEnabled = false
        binding.surname2.isEnabled = false

    }

    private fun editingState(it: EditPersonState.EditMode) {
        binding.progressBar.isVisible = false
        if (it.relationship is SingleModel) {
            binding.name.setText(it.relationship.person.name)
            binding.surname1.setText(it.relationship.person.surname1)
            binding.surname2.setText(it.relationship.person.surname2)
        }

    }
    private fun errorState() {
        binding.progressBar.isVisible = false

    }
}