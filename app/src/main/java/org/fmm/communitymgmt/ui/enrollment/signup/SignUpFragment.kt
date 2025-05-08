package org.fmm.communitymgmt.ui.enrollment.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.databinding.FragmentSignUpBinding
import org.fmm.communitymgmt.domainmodels.model.Genders
import org.fmm.communitymgmt.domainmodels.model.UserInfoModel
import org.fmm.communitymgmt.ui.security.model.UserSession

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private val signUpViewModel by viewModels<SignUpViewModel>()

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    // Para recibir parámetros, ya no hace falta
//    private val args: SignUpFragmentArgs by navArgs()
    private lateinit var iLoader: ImageLoader

    private lateinit var userSession: UserSession
    private lateinit var userInfo: UserInfoModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iLoader = ImageLoader.Builder(requireContext()).build()

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModelFMM = signUpViewModel
        binding.formFMM = signUpViewModel.formSignUpState.value

        initData()
        initUI()
    }

    private fun initData() {
        signUpViewModel.initData()
        userSession = signUpViewModel.userSession
        userInfo = userSession.userInfo!!
    }

    private fun initUI() {
//        initToolbar()
        updateButtons()
        initListeners()
        initUIState()
    }

    private fun updateButtons() {
        val lastStep = binding.vfSignUpForm.displayedChild == binding.vfSignUpForm.childCount -1
        val firstStep = binding.vfSignUpForm.displayedChild ==0

        binding.llStepsButtons.isVisible = !lastStep
        binding.llFinalButtons.isVisible = lastStep

        binding.btnNext.isEnabled = !lastStep && signUpViewModel.formSignUpState.value.isValid()
        binding.btnPrevious.isEnabled = !firstStep

        binding.btnFinalNext.isEnabled = false
        binding.btnFinalPrevious.isEnabled = !firstStep

        binding.btnAccept.isEnabled = signUpViewModel.formSignUpState.value.isValid()
    }

    private fun initListeners() {
        binding.btnNext.setOnClickListener {
            if (binding.vfSignUpForm.displayedChild < binding.vfSignUpForm.childCount -1) {
                signUpViewModel.formSignUpState.value.currentStep +=1
                binding.vfSignUpForm.showNext()
                updateButtons()
            }
        }
        binding.btnPrevious.setOnClickListener {
            if (binding.vfSignUpForm.displayedChild > 0) {
                signUpViewModel.formSignUpState.value.currentStep -=1
                binding.vfSignUpForm.showPrevious()
                updateButtons()
            }
        }

        binding.btnFinalPrevious.setOnClickListener {
            if (binding.vfSignUpForm.displayedChild > 0) {
                signUpViewModel.formSignUpState.value.currentStep -=1
                binding.vfSignUpForm.showPrevious()
                updateButtons()
            }
        }

        binding.btnAccept.setOnClickListener {
            signUpViewModel.onAcceptClick()
        }

    }
    /**
     * Como tiene ViewModel y StateFlow, este método configura el collect del state
     * Lo asocia a un estado del ciclo de vida y lo mete en una corrutina
     */
    private fun initUIState() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                signUpViewModel.uiSignUpSate.collect {
                    when (it) {
                        SignUpUIState.Loading -> loadingState()
                        is SignUpUIState.EditMode -> editingState(it)
                        is SignUpUIState.Error -> errorState(it)
                        is SignUpUIState.RegisteredMode -> registeredState(it)
                    }
                    updateButtons()
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                signUpViewModel.formSignUpState.collect {
                    binding.formFMM = signUpViewModel.formSignUpState.value
                    updateButtons()
                }
            }
        }

    }

    /**
     * States
     */

    private fun loadingState() {
        binding.pb.isVisible = true
    }

    private fun editingState(it: SignUpUIState.EditMode) {
        binding.pb.isVisible = false

        // Cuando se reciben los datos en el modelo, se instancia el UIState
        //
        // Cuando se hace el collect, es necesario volver a asignar el formulario
        //binding.formFMM = editPersonViewModel.formState.value
        //it.relationship
        updateButtons()
        /*
        if (userInfo.imageUrl.isNotBlank() && userInfo.imageUrl
            .isNotEmpty()) {
            val request = ImageRequest.Builder(requireContext())
                .data(userInfo.imageUrl)
                .target(binding.userInfoPhoto)
                .build()

            iLoader.enqueue(request)
        }

         */

    }

    private fun registeredState(it: SignUpUIState.RegisteredMode) {
        findNavController().navigate(
            SignUpFragmentDirections.actionSignUpFragmentToCommunityEnrollmentFragment()
        )
    }

    private fun errorState(editPersonUIState: SignUpUIState.Error) {
        binding.pb.isVisible = false

        Toast.makeText(
            requireContext(), "Se ha producido un error preparando la edición", Toast
                .LENGTH_SHORT
        ).show()

    }


    /**
     * Bindings
     */
    //    fun bindName(view: EditText, state: EditPersonFormState?, callback: ((String) -> Unit)) {
    companion object {
/*
        @BindingAdapter("formName", "onNameChanged", requireAll = true)
        @JvmStatic
        fun bindName(
            view: EditText, state: SignUpFormState?, onChanged: SignUpViewModel.OnTextChangedFMM?
        ) {
            if (state == null || onChanged == null) return
            if (view.text.toString() != state.name) {
                view.setText(state.name)
            }

            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                }

                override fun afterTextChanged(s: Editable?) {
                    val newText = s?.toString().orEmpty()
                    if (newText != state.name) {
                        onChanged.onChangedText(newText)
                    }
                }
            }

            val existingWatcher = view.getTag(R.id.edit_text_watcher_tag) as? TextWatcher
            if (existingWatcher != null) {
                view.removeTextChangedListener(existingWatcher)
            }
            // Esta forma no funciona
            // view.removeTextChangedListener(textWatcher)
            view.addTextChangedListener(textWatcher)
            view.setTag(R.id.edit_text_watcher_tag, textWatcher)
        }

        @BindingAdapter("formSurname1", "onSurname1Changed", requireAll = true)
        @JvmStatic
        fun bindSurname1(
            view: EditText, state: SignUpFormState?, onChanged: SignUpViewModel.OnTextChangedFMM?
        ) {
            if (state == null || onChanged == null) return
            if (view.text.toString() != state.surname1) {
                view.setText(state.surname1)
            }

            val existingWatcher = view.getTag(R.id.edit_text_watcher_tag) as? TextWatcher
            if (existingWatcher != null) {
                view.removeTextChangedListener(existingWatcher)
            }

            val textWatcher = object: TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                }

                override fun afterTextChanged(s: Editable?) {
                    val newText = s?.toString().orEmpty()
                    if (newText != state.surname1) {
                        onChanged.onChangedText(newText)
                    }
                }

            }
            view.addTextChangedListener(textWatcher)
            view.setTag(R.id.edit_text_watcher_tag, textWatcher)
        }

        @BindingAdapter("formSurname2", "onSurname2Changed", requireAll = true)
        @JvmStatic
        fun bindSurname2(
            view: EditText, state: SignUpFormState?, onChanged: SignUpViewModel.OnTextChangedFMM?
        ) {
            if (state == null || onChanged == null) return
            if (view.text.toString() != state.surname2) {
                view.setText(state.surname2)
            }

            val existingWatcher = view.getTag(R.id.edit_text_watcher_tag) as? TextWatcher
            if (existingWatcher != null) {
                view.removeTextChangedListener(existingWatcher)
            }

            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val newText = s?.toString().orEmpty()
                    if (newText != state.surname2) {
                        onChanged.onChangedText(newText)
                    }
                }
            }
            view.addTextChangedListener(textWatcher)
            view.setTag(R.id.edit_text_watcher_tag, textWatcher)
        }

 */

        @BindingAdapter("formFieldValue", "onFieldChanged", requireAll = false)
        @JvmStatic
        fun bindGenericaField(
            view: EditText, value: String?, onChanged: SignUpViewModel.OnTextChangedFMM?
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

                val otroWatcher = GenericTextWatcher(value, onChanged) { newText ->
                    newText != value
                }

                view.addTextChangedListener(otroWatcher)
                view.setTag(R.id.edit_text_watcher_tag, otroWatcher)
            }
        }

        @BindingAdapter("formBooleanValue", "onBooleanChanged", requireAll = true)
        @JvmStatic
        fun bindGenericFieldBoolean(
            view: CheckBox, value: Boolean,
            onChanged: SignUpViewModel.OnBooleanChangeFMM?
        ) {
            if (view.isChecked != value) {
                view.isChecked = value
            }

            // Quita listener anterior si lo hubiera
            val existingListener = view.getTag(R.id.switch_listener_tag) as? CompoundButton.OnCheckedChangeListener
            if (existingListener != null) {
                view.setOnCheckedChangeListener(null)
            }

            // ✅ Define el listener como variable
            val listener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
                onChanged?.onChangedBoolean(isChecked)
            }

            // Asigna y guarda
            view.setOnCheckedChangeListener(listener)
            view.setTag(R.id.switch_listener_tag, listener)

        }

        @BindingAdapter("formGenderValue", "onRadioGroupChanged", requireAll = false)
        @JvmStatic
        fun bindGenericFieldRadioGroup(
            view: RadioGroup,
            selectedValue: Genders?,
            onChanged: SignUpViewModel.OnGenderChangeFMM?
        ) {
//            if (selectedValue == null) return

            if (selectedValue != null) {
                for (i in 0 until view.childCount) {
                    val child = view.getChildAt(i)
                    if (child is RadioButton && child.tag == selectedValue && !child.isChecked) {
                        child.isChecked = true
                        break
                    }
                }
            }
            // Quita listener anterior si lo hubiera
            val existingListener = view.getTag(R.id.radio_group_listener_tag) as? RadioGroup.OnCheckedChangeListener
            if (existingListener != null) {
                view.setOnCheckedChangeListener(null)
            }

            // ✅ Define el listener como variable
            val listener = RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val checkedButton = group.findViewById<RadioButton>(checkedId)
                val newValue = checkedButton.tag as? Genders
                if (newValue != selectedValue) {
                    onChanged?.onChangeGender(newValue!!)
                }
            }

            // Asigna y guarda
            view.setOnCheckedChangeListener(listener)
            view.setTag(R.id.switch_listener_tag, listener)

        }

    }
}

class GenericTextWatcher(val value:String,
                         private val onChanged: SignUpViewModel.OnTextChangedFMM?,
                         val functi: (a:String) -> Boolean):
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
        if (functi(newText)) {
            onChanged!!.onChangedText(newText)
        }
    }
}
