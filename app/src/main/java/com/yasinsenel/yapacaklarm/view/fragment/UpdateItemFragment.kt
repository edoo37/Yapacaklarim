package com.yasinsenel.yapacaklarm.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import com.yasinsenel.yapacaklarm.R
import com.yasinsenel.yapacaklarm.databinding.FragmentMainBinding
import com.yasinsenel.yapacaklarm.databinding.FragmentUpdateItemBinding
import com.yasinsenel.yapacaklarm.model.TodoData
import com.yasinsenel.yapacaklarm.viewmodel.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateItemFragment : Fragment() {

    private lateinit var binding : FragmentUpdateItemBinding
    private var todoModel: TodoData? = TodoData()
    private val mainFragmentViewModel : MainFragmentViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundleData = arguments
        todoModel = bundleData?.getParcelable("dataClass")
        val getid = todoModel?.id
        binding.edtTaskName.setText(todoModel?.todoName)
        binding.edtTaskDesc.setText(todoModel?.todoDesc)
        binding.edtDate.setText(todoModel?.todoDate)
        binding.imageView.setImageURI(todoModel?.todoImage?.toUri())

        binding.btnConfirm.setOnClickListener {
            val list = TodoData("asd","asd","31","31","31",getid)
            mainFragmentViewModel.updateItem(list)
        }


    }

}