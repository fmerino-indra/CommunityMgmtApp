package org.fmm.communitymgmt.ui.enrollment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import org.fmm.communitymgmt.R

class ResponsibleDialog(private val listener2: NoticeDialogListener): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.isResponsible)
                .setPositiveButton("Yes") { dialog, id ->
                    listener2.onDialogPositiveClick(this)
                }
                .setNegativeButton("No") { dialog, id ->
                    listener2.onDialogNegativeClick(this)
                }
            builder.create()
        }?:throw IllegalStateException("Activity cannot be null")
    }
    internal lateinit var listener: NoticeDialogListener

    interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }
/*
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as NoticeDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(("$context must implement NoticeDialogListener"))
        }
    }

 */
}