package com.yasinsenel.yapacaklarm.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.orhanobut.hawk.Hawk
import com.yasinsenel.yapacaklarm.R
import com.yasinsenel.yapacaklarm.adapter.TodoAdapter
import com.yasinsenel.yapacaklarm.databinding.FragmentMainBinding
import com.yasinsenel.yapacaklarm.model.TodoData
import com.yasinsenel.yapacaklarm.util.RemindWorker
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class MainFragment : Fragment() {
    private lateinit var binding : FragmentMainBinding
    private var todoModel: TodoData? = TodoData()
    private val todoAdapter = TodoAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Hawk.init(requireContext()).build()


        binding.fab.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_addTaskFragment)
        }


        setAdapter()
    }

    private fun setAdapter(){
        binding.apply {
            val oldList : ArrayList<TodoData> = Hawk.get("myData2", arrayListOf())
            val newList : ArrayList<TodoData> = Hawk.get("myData2", arrayListOf())
            recyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            recyclerView.adapter = todoAdapter
            todoAdapter.setData(oldList)
            todoAdapter.setNewList(newList)
        }
    }

}