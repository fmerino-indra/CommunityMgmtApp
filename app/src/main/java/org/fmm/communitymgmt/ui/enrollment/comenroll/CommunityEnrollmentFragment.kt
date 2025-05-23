package org.fmm.communitymgmt.ui.enrollment.comenroll

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.databinding.FragmentCommunityEnrollmentBinding
import org.fmm.communitymgmt.ui.common.AddressViewModel
import org.fmm.communitymgmt.ui.common.UserInfoViewModel
import org.fmm.communitymgmt.ui.enrollment.YesNoBottomSheetDialogFragment

@AndroidEntryPoint
class CommunityEnrollmentFragment : Fragment() {

    private val communityEnrollmentViewModel: CommunityEnrollmentViewModel by viewModels()
    // @TODO Revisar si es necesario en esta activity
    private val userInfoViewModel: UserInfoViewModel by activityViewModels<UserInfoViewModel>()

    private var _binding: FragmentCommunityEnrollmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityEnrollmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initUI()

//        askResponsible()
    }
    private fun initData() {

        binding.lifecycleOwner = viewLifecycleOwner
        binding.communityEnrollmentViewModel = communityEnrollmentViewModel
        binding.formCommunityEnrollment = communityEnrollmentViewModel
            .formCommunityEnrollmentState.value

        binding.parishAddress.addressCallback = communityEnrollmentViewModel.addressViewModel
        binding.parishAddress.addressForm = communityEnrollmentViewModel.formAddressState.value
        communityEnrollmentViewModel.initData(userInfoViewModel)
    }
    private fun initUI() {
        initListeners()
        initUIState()
    }
    private fun initListeners() {
        binding.btnAccept.setOnClickListener {
            communityEnrollmentViewModel.onAcceptClick()
        }


    }
    private fun initUIState() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                communityEnrollmentViewModel.uiCommunityEnrollmentSate.collect {
                    when (it) {
                        CommunityEnrollmentUIState.Loading -> loadingState()
                        CommunityEnrollmentUIState.EditMode -> editingState()
                        is CommunityEnrollmentUIState.Error -> TODO()
                        CommunityEnrollmentUIState.RegisteredMode -> registeredState(CommunityEnrollmentUIState.RegisteredMode)
                        CommunityEnrollmentUIState.MarriedMode -> marriedState()
                        CommunityEnrollmentUIState.SingleMode -> singleState()
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                communityEnrollmentViewModel.formCommunityEnrollmentState.collect {
                    binding.formCommunityEnrollment = communityEnrollmentViewModel.formCommunityEnrollmentState.value
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                communityEnrollmentViewModel.formAddressState.collect {
                    binding.parishAddress.addressForm = it
                    communityEnrollmentViewModel.onAddressChanged(it)
                }
            }
        }

    }

    /**
     * States
     */
    private fun singleState() {
    }

    private fun marriedState() {
        // Navega a BrothersEnrollment creando una petición para el cónyuge
    }
    private fun registeredState(it: CommunityEnrollmentUIState.RegisteredMode) {
        askMarried()
        /*
        findNavController().navigate(
            CommunityEnrollmentFragmentDirections.actionCommunityEnrollmentFragmentToHomeActivity2()
        )

         */
    }
    private fun editingState() {
        binding.pb.isVisible = false
    }

    private fun loadingState() {
        binding.pb.isVisible = true
    }

    private fun askMarried() {
        val dialog = YesNoBottomSheetDialogFragment(getString(R.string.isMarried)) {
                isMarried ->
            if (isMarried) {


            } else {
                findNavController().navigate(
                    CommunityEnrollmentFragmentDirections.actionCommunityEnrollmentFragmentToBrothersEnrollmentFragment()
                )
                // Navegar a BrothersEnrollment sin petición
            }
        }
        dialog.show(parentFragmentManager, getString(R.string.important_question))

    }


/*
    private fun askResponsible() {
        val responseCallback:YesNoDialog.NoticeDialogListener = object:YesNoDialog
        .NoticeDialogListener {
            override fun onDialogPositiveClick(dialog: DialogFragment) {
                Log.d("dialogListener", "Ha pulsado Sí")
                communityEnrollmentViewModel.onIsResponsibleChanged(true)
            }

            override fun onDialogNegativeClick(dialog: DialogFragment) {
                Log.d("dialogListener", "Ha pulsado No")
                communityEnrollmentViewModel.onIsResponsibleChanged(false)
            }
        }
        val newFragment = YesNoDialog(responseCallback,requireContext().getString(R.string
            .isResponsible))
        newFragment.show(parentFragmentManager, "isResponsible")
    }
*/
    /**
     * Bindings
     */
    companion object {

        @BindingAdapter("formFieldValue", "onStringChanged", requireAll = true)
        @JvmStatic
        fun bindGenericTextField(
            view: EditText, value: String?, onChanged: CommunityEnrollmentViewModel.OnTextChangedFMM?
        ) {
            if (value == null) return
            if (view.text.toString() != value) {
                view.setText(value)
            }
//R.id.parishName
            if (onChanged != null) {
                val existingWatcher = view.getTag(R.id.edit_text_watcher_tag) as? TextWatcher
                if (existingWatcher != null) {
                    view.removeTextChangedListener(existingWatcher)
                }

                val otroWatcher = GenericTextWatcher(value, onChanged) { newText ->
                    newText != value
                }

                view.addTextChangedListener(otroWatcher)
                view.setTag(R.id.edit_text_watcher_tag, otroWatcher)
            }
        }

        @BindingAdapter("formFieldValue", "onBooleanChanged", requireAll = true)
        @JvmStatic
        fun bindGenericBoleanField(
            view: CheckBox, value: Boolean, onChanged: CommunityEnrollmentViewModel.OnBooleanChangeFMM?
        ) {
            if (view.isChecked != value) {
                view.isChecked = value
            }

            // ✅ Define el listener como variable
            val listener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
                onChanged?.onChangedBoolean(isChecked)
            }
            view.setOnCheckedChangeListener(listener)
        }

        @BindingAdapter("addressFormFieldValue", "onStringChanged", requireAll = true)
        @JvmStatic
        fun bindAddressTextField(
            view: EditText, value: String?, onChanged: AddressViewModel.OnAddressTextChanged?
        ) {
            if (value == null) return
            if (view.text.toString() != value) {
                view.setText(value)
            }

            if (onChanged != null) {
                val existingWatcher = view.getTag(R.id.edit_text_watcher_tag) as? TextWatcher
                if (existingWatcher != null) {
                    view.removeTextChangedListener(existingWatcher)
                }

                val otroWatcher = AddressTextWatcher(value, onChanged) { newText ->
                    newText != value
                }

                view.addTextChangedListener(otroWatcher)
                view.setTag(R.id.edit_text_watcher_tag, otroWatcher)
            }
        }

    }



    class GenericTextWatcher(val value:String,
                             private val onChanged: CommunityEnrollmentViewModel.OnTextChangedFMM?,
                             val callback: (a:String) -> Boolean):
        TextWatcher {

        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            val newText = s?.toString().orEmpty()
            if (callback(newText)) {
                onChanged!!.onChangedText(newText)
            }
        }
    }

    class AddressTextWatcher(val value:String,
                             private val onChanged: AddressViewModel.OnAddressTextChanged?,
                             val callback: (a:String) -> Boolean):
        TextWatcher {

        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            val newText = s?.toString().orEmpty()
            if (callback(newText)) {
                onChanged!!.onChangedText(newText)
            }
        }
    }
}