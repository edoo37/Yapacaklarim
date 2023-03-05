package com.yasinsenel.yapacaklarm.presentation.timepicker

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.yasinsenel.yapacaklarm.R
import java.util.*


class TimePickerFragment(private val listener : (String) -> Unit) : DialogFragment(), TimePickerDialog.OnTimeSetListener{
    private val calendar = Calendar.getInstance()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val dialog = TimePickerDialog(activity as Context,this,hour,minute,false)

        return dialog

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_picker, container, false)
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        listener("${p1}:${p2}")
    }

}