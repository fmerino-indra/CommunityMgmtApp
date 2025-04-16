package org.fmm.communitymgmt.ui.comlist.detail

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.databinding.FragmentEditPersonBinding
import org.fmm.communitymgmt.domainmodels.model.SingleModel

@AndroidEntryPoint
class EditPersonFragment : Fragment() {
    private val editPersonViewModel by viewModels<EditPersonViewModel>()

    private lateinit var _binding: FragmentEditPersonBinding
    private val binding: FragmentEditPersonBinding get() = _binding

    private val args: EditPersonFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        // Inflate with DataBindingUtil. @TODO Repair warning
        _binding = DataBindingUtil.inflate(
            layoutInflater, R.layout
                .fragment_edit_person,
            container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModelFMM = editPersonViewModel
        binding.formFMM = editPersonViewModel.formState.value

        initData()
        initUI()
    }

    private fun initData() {
        Log.d("[FMMP - EditPersonFragment", "El valor pasado es: \n id=${args.relationshipId}")
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
/*
Esto falla en ejecución, dice que no lo encuentra.
android:onClick="@{() -> viewModelFMM.onAcceptClick()"

Lo hacemos con setOnClickLIstener
 */
    private fun onAccept() {
        editPersonViewModel.onAcceptClick()
    }

    /**
     * Como tiene ViewModel y StateFlow, este método configura el collect del state
     * Lo asocia a un estado del ciclo de vida y lo mete en una corrutina
     */
    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                editPersonViewModel.uiState.collect {
//                    binding.formFMM = editPersonViewModel.formState.value

                    when (it) {
                        EditPersonUIState.Loading -> loadingState()
                        is EditPersonUIState.ViewMode -> viewingState(it)
                        is EditPersonUIState.EditMode -> editingState(it)
                        is EditPersonUIState.Error -> errorState(it)
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                editPersonViewModel.formState.collect {
                    binding.formFMM = editPersonViewModel.formState.value
                }
            }
        }

    }

    private fun loadingState() {
        binding.progressBar.isVisible = true
    }

    private fun viewingState(it: EditPersonUIState.ViewMode) {
        binding.progressBar.isVisible = false
        binding.btnAccept.isVisible = false
        binding.btnCancel.setText("Back")
        if (it.relationship is SingleModel) {
//            binding.name.setText(it.relationship.person.name)
            binding.surname1.setText(it.relationship.person.surname1)
            binding.surname2.setText(it.relationship.person.surname2)
        }

        binding.name.isEnabled = false
        binding.surname1.isEnabled = false
        binding.surname2.isEnabled = false

    }

    private fun editingState(it: EditPersonUIState.EditMode) {
        // Cuando se reciben los datos en el modelo, se instancia el UIState
        //
        // Cuando se hace el collect, es necesario volver a asignar el formulario
        //binding.formFMM = editPersonViewModel.formState.value
        //it.relationship

        binding.progressBar.isVisible = false
        if (it.relationship is SingleModel) {
//            binding.name.setText(it.relationship.person.name)
//            binding.surname1.setText(it.relationship.person.surname1)
//            binding.surname2.setText(it.relationship.person.surname2)
        }

    }

    private fun errorState(editPersonUIState: EditPersonUIState.Error) {
        binding.progressBar.isVisible = false
        Toast.makeText(
            requireContext(), "Se ha producido un error preparando la edición", Toast
                .LENGTH_SHORT
        ).show()

    }

    //    fun bindName(view: EditText, state: EditPersonFormState?, callback: ((String) -> Unit)) {
    companion object {

        @BindingAdapter("formName", "onNameChanged", requireAll = true)
        @JvmStatic
        fun bindName(
            view: EditText, state: EditPersonFormState?, onChanged: EditPersonViewModel
            .OnTextChangedFMM?
        ) {
            if (state == null || onChanged == null) return
            Log.d("[FMMP] [bindName]", "El valor de state.name se ha establecido" +
                    "\n ${state.name}")
            if (view.text.toString() != state.name) {
                Log.d("[FMMP] [EditPersonFragment - BindingAdapter]", "El valor de state.name ha " +
                        "cambiado\n ${state.name}")
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
                        onChanged.onChangedFMM(newText)
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
            view: EditText, state: EditPersonFormState?, onChanged: EditPersonViewModel
            .OnTextChangedFMM?
        ) {
            if (state == null || onChanged == null) return
            Log.d("[FMMP] [bindName]", "El valor de state.surname1 se ha establecido" +
                    "\n ${state.surname1}")
            if (view.text.toString() != state.surname1) {
                Log.d("[FMMP] [EditPersonFragment - BindingAdapter]", "El valor de state.surname1" +
                        " ha cambiado\n ${state.surname1}")
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
                        onChanged.onChangedFMM(newText)
                    }
                }

            }
            view.addTextChangedListener(textWatcher)
            view.setTag(R.id.edit_text_watcher_tag, textWatcher)
        }

        @BindingAdapter("formSurname2", "onSurname2Changed", requireAll = true)
        @JvmStatic
        fun bindSurname2(
            view: EditText, state: EditPersonFormState?, onChanged: EditPersonViewModel
            .OnTextChangedFMM?
        ) {
            if (state == null || onChanged == null) return
            Log.d("[FMMP] [bindName]", "El valor de state.surname2 se ha establecido" +
                    "\n ${state.surname2}")
            if (view.text.toString() != state.surname2) {
                Log.d("[FMMP] [EditPersonFragment - BindingAdapter]", "El valor de state.surname2" +
                        " ha cambiado\n ${state.surname2}")
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
                        onChanged.onChangedFMM(newText)
                    }
                }
            }
            view.addTextChangedListener(textWatcher)
            view.setTag(R.id.edit_text_watcher_tag, textWatcher)
        }
    }
}

/*
Listener class kotlin.jvm.functions.Function1 with method invoke did not match signature of any method viewModelFMM::onNameChanged
        app:onNameChanged="@{viewModelFMM::onNameChanged}"

Listener class 'kotlin. jvm. functions. Function1<? super java. lang. String,kotlin. Unit>' with method 'invoke'
did not match signature of any method 'app:onNameChanged'
        app:onNameChanged="@{value -> viewModelFMM.onNameChanged(value)}"

 */