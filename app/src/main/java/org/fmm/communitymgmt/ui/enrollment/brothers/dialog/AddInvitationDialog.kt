package org.fmm.communitymgmt.ui.enrollment.brothers.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import org.fmm.communitymgmt.databinding.FragmentAddInvitationDialogBinding
import org.fmm.communitymgmt.ui.enrollment.brothers.BrothersEnrollmentViewModel

class AddInvitationDialog(private val viewModel: BrothersEnrollmentViewModel): DialogFragment() {
    private var _bindingDialog: FragmentAddInvitationDialogBinding? = null
    private val bindingDialog get() = _bindingDialog!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingDialog = FragmentAddInvitationDialogBinding.inflate(layoutInflater, container, false)
        return bindingDialog.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingDialog.btnCreateInvitation.setOnClickListener {
            viewModel.onAddInvitation(bindingDialog.etInvitationName.text.toString(),
            bindingDialog.etExpiration.text.toString().toInt(), bindingDialog.isMarriage.isChecked )
            this.dismiss()
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.requestWindowFeature(Window.DECOR_CAPTION_SHADE_AUTO)
//        dialog.setTitle("Add invitations")
        return dialog
    }
}
