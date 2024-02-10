package com.ava.alivakili.drink_water.tracking

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
import android.widget.EditText
import com.ava.alivakili.drink_water.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddWaterAmountBottomSheet (private val onItemAdded:(Int)->Unit): BottomSheetDialogFragment() {

    private var editText: EditText?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val view = inflater.inflate(R.layout.add_water_amount_bottom_cheet,container,false)
        editText=view.findViewById(R.id.waterAmountCreationEditText)

        editText?.requestFocus()
        dialog?.window?.setSoftInputMode(SOFT_INPUT_STATE_VISIBLE)
        editText?.setOnEditorActionListener{textView,i,keyEvent->
            val text=textView.text.toString().trim()
            if(text.isNotEmpty()) onItemAdded(text.toInt())
            dismiss()
            true
        }

        return view


    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        editText?.text?.clear()
    }

    companion object{
        const val TAG="AddWaterAmountBottomSheet"
    }


}