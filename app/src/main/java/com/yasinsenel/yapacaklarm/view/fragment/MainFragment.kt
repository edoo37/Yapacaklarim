package com.yasinsenel.yapacaklarm.view.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.orhanobut.hawk.Hawk
import com.yasinsenel.yapacaklarm.R
import com.yasinsenel.yapacaklarm.adapter.TodoAdapter
import com.yasinsenel.yapacaklarm.databinding.FragmentMainBinding
import com.yasinsenel.yapacaklarm.model.TodoData
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


class MainFragment : Fragment() {
    private lateinit var binding : FragmentMainBinding
    private var todoModel: TodoData? = TodoData()
    private val todoAdapter = TodoAdapter()
    private var newList : ArrayList<TodoData> = arrayListOf()
    private var oldList : ArrayList<TodoData> = arrayListOf()
    private val filteredList : ArrayList<TodoData> = arrayListOf()
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



        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                search(newText)
                return false
            }
        })

        val toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.main_menu,menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.about -> {
                        // clearCompletedTasks()
                        true
                    }
                    R.id.settings -> {
                        Navigation.findNavController(requireView()).navigate(R.id.action_mainFragment_to_settingsFragment)
                        true
                    }
                    else -> false
                }
            }

        })

        binding.fab.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_addTaskFragment)
        }
        checkAppMode()

        setAdapter()
    }

    private fun setAdapter(){
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            recyclerView.adapter = todoAdapter
            oldList = Hawk.get("myData2", arrayListOf())
            newList = Hawk.get("myData2", arrayListOf())
            if(newList.size == 0){
                binding.tvEmpty.visibility = View.VISIBLE
                println("Deneme")
            }
            else{
                binding.tvEmpty.visibility = View.INVISIBLE
            }
            todoAdapter.setData(oldList)
            todoAdapter.setNewList(newList)
        }
    }

    private fun checkAppMode(){
        val anan = Hawk.get("mode",false)
        val isSelected = Hawk.get("isSelected",false)

        val date2 = Hawk.get("selectedLang","tr")
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale(date2))
        val date = Date()
        sdf.applyPattern("EEE, d, yyyy, HH:mm")
        val str = sdf.format(date)
        val split = str.split(",")
        val day = split.get(0)
        val dayNum = split.get(1)
        val dayTime = split.get(3)
        binding.tvDate.text = day + dayNum
        binding.tvTime.text = dayTime


        if(isSelected){
            if(anan){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        else{
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    Hawk.put("mode",true)
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    Hawk.put("mode",false)
                }
            }
        }


    }


    private fun search(text : String?){
        filteredList.clear()
        if(text?.length!! >=3){
            newList.forEach {
                if(it.todoName.lowercase().startsWith(text.lowercase())){
                    filteredList.add(it)
                }
            }
            todoAdapter.setNewList(filteredList)
        }
        else{
            todoAdapter.setNewList(newList)
        }

    }


}