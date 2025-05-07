package org.fmm.communitymgmt.ui.enrollment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import org.fmm.communitymgmt.R

class YesNoDialog(private val listener2: NoticeDialogListener, private val question: String):
    DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(question)
                .setPositiveButton("Yes") { dialog, id ->
                    listener2.onDialogPositiveClick(this)
                }
                .setNegativeButton("No") { dialog, id ->
                    listener2.onDialogNegativeClick(this)
                }
            builder.create()
        }?:throw IllegalStateException("Activity cannot be null")
    }
    interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }
}