package com.yasinsenel.yapacaklarm.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.orhanobut.hawk.Hawk
import com.yasinsenel.yapacaklarm.R
import com.yasinsenel.yapacaklarm.databinding.FragmentAddTaskBinding
import com.yasinsenel.yapacaklarm.model.TodoData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddTaskFragment : Fragment() {
    private lateinit var binding : FragmentAddTaskBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edtDate.setOnFocusChangeListener { view, b ->
            if(view.isInTouchMode && b){
                view.performClick()
            }
        }

        binding.apply {
            btnConfirm.setOnClickListener {
                val addList : ArrayList<TodoData> = Hawk.get("data", arrayListOf())
                val date = binding.edtDate.text.toString()
                addList.add(TodoData(binding.edtTaskName.text.toString(),date))
                Hawk.put("data",addList)
                Navigation.findNavController(view).navigate(R.id.action_addTaskFragment_to_mainFragment)
            }

            edtDate.setOnClickListener {
                val datePickerFragment = DataPickerFragment()
                val supportFragment = requireActivity().supportFragmentManager

                supportFragment.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                ){ resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY") {
                        val date = bundle.getString("SELECTED_DATE")
                        binding.edtDate.setText(date)
                    }
                }
                datePickerFragment.show(supportFragment,"DatePickerFragment")
            }
        }
    }

}