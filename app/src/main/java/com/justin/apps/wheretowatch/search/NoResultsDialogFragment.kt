package com.justin.apps.wheretowatch.search

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.justin.apps.wheretowatch.R
import kotlinx.android.synthetic.main.fragment_dialog_noresults.*

class NoResultsDialogFragment: DialogFragment() {
    private val TAG = "NoResultsDialogFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dialog_noresults, container, false)

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        action_okay.setOnClickListener{
            Log.d(TAG, "Closing dialog")
            dialog.dismiss()
        }
    }
}