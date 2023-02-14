package com.yasinsenel.yapacaklarm.view.fragment

import android.content.ContentResolver
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.net.toUri
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Database
import androidx.work.*
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.orhanobut.hawk.Hawk
import com.yasinsenel.yapacaklarm.R
import com.yasinsenel.yapacaklarm.adapter.TodoAdapter
import com.yasinsenel.yapacaklarm.databinding.FragmentMainBinding
import com.yasinsenel.yapacaklarm.model.TodoData
import com.yasinsenel.yapacaklarm.utils.removeWorkReqeust
import com.yasinsenel.yapacaklarm.viewmodel.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class MainFragment : Fragment(), TodoAdapter.removeItem {
    private var binding : FragmentMainBinding? = null
    private lateinit var todoAdapter : TodoAdapter
    private var setList : MutableList<TodoData>? = mutableListOf()
    private var newList : MutableList<TodoData>? = mutableListOf()
    private val filteredList : MutableList<TodoData> = mutableListOf()
    private val list : ArrayList<String> = arrayListOf()
    private val mainFragmentViewModel : MainFragmentViewModel by viewModels()
    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference
    private var userData : Array<String>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentMainBinding.inflate(inflater, container, false)
        Log.d("myview",binding.toString())
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Hawk.init(requireContext()).build()

        checkAppMode()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })




        todoAdapter = TodoAdapter(mainFragmentViewModel,this@MainFragment)
        getImageFromAPI()
        mainFragmentViewModel.getAllData()
        mainFragmentViewModel.getRoomList.observe(viewLifecycleOwner) {
            it?.let {
                setList = it
                println(it)
            }
            database = Firebase.database.reference
            var exampleArray = setList?.toList()
            newList = exampleArray?.filter { it.userId == auth.currentUser?.uid }?.toMutableList()
            database.child("users").child(auth.currentUser?.uid!!).child("todoList").setValue(newList)
            setAdapter(newList)
        }

        val args = arguments
        val bundle = args?.getStringArray("sendUserData")
        binding!!.tvUsername.text = bundle?.get(0)





        binding!!.recyclerView.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener{
            override fun onChildViewAttachedToWindow(view: View) {
               //binding.tvEmpty.visibility = View.INVISIBLE
            }

            override fun onChildViewDetachedFromWindow(view: View) {
               //binding.tvEmpty.visibility = View.VISIBLE

            }

        })

        binding!!.ivUser.setOnClickListener {
            auth.signOut()
            Navigation.findNavController(requireView()).navigate(R.id.action_mainFragment_to_loginRegisterFragment)
        }


        binding!!.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                search(newText)
                return false
            }
        })


        val toolbar = binding!!.toolbar

        toolbar.inflateMenu(R.menu.main_menu)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.about -> {
                    Navigation.findNavController(requireView()).navigate(R.id.action_mainFragment_to_aboutFragment)
                    true
                }
                R.id.settings -> {
                    Navigation.findNavController(requireView()).navigate(R.id.action_mainFragment_to_settingsFragment)
                    true
                }
                R.id.logout ->{
                    Navigation.findNavController(requireView()).navigate(R.id.action_mainFragment_to_loginRegisterFragment)
                    true
                }
                else -> false
            }
        }
        /*(requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                if(menu.size()==0){
                    menu.clear()
                    Toast.makeText(requireContext(),"asd",Toast.LENGTH_SHORT).show()
                    menuInflater.inflate(R.menu.main_menu,menu)
                }

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.about -> {
                        Navigation.findNavController(requireView()).navigate(R.id.action_mainFragment_to_aboutFragment)
                        true
                    }
                    R.id.settings -> {
                        Navigation.findNavController(requireView()).navigate(R.id.action_mainFragment_to_settingsFragment)
                        true
                    }
                    R.id.logout ->{

                        true
                    }
                    else -> false
                }
            }

        })*/

        binding!!.fab.setOnClickListener {
            val user = auth.currentUser?.uid
            println(user)
            val bundle = Bundle()
            bundle.putString("userid",user)
            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_addTaskFragment,bundle)
        }


    }

    private fun setAdapter(setmyList : MutableList<TodoData>?){

        binding!!.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            recyclerView.adapter = todoAdapter
            todoAdapter.setNewList(setmyList!!)
            todoAdapter.setData(setmyList!!)

            if(setmyList!!.size>0){
                binding!!.tvEmpty.visibility = View.INVISIBLE
            }
            else{
                binding!!.tvEmpty.visibility = View.VISIBLE
            }

        }
    }

    private fun checkAppMode(){
        val anan = Hawk.get("mode",false)
        val isSelected = Hawk.get("isSelected",false)

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
        println(list)
        if(text?.length!! >=3){
            setList?.forEach {
                if(it.todoName?.lowercase()!!.startsWith(text.lowercase())){
                    filteredList.add(it)
                }
            }
            todoAdapter.setNewList(filteredList)
        }
        else{
            todoAdapter.setNewList(setList!!)
        }

    }


    private fun getImageFromAPI(){
        mainFragmentViewModel.getWeatherData("nature")
        mainFragmentViewModel.getImageData.observe(viewLifecycleOwner){
            it?.let {
                println(it.alt_description)
                Glide.with(requireContext())
                    .load(it.urls.full)
                    .into(binding!!.ivMain)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onStart() {
        super.onStart()

        val date2 = Hawk.get("selectedLang","tr")
        GlobalScope.launch {
            val runnable = object : Runnable{
                override fun run() {
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale(date2))
                    val date = Date()
                    sdf.applyPattern("EEE, d, yyyy, HH:mm")
                    val str = sdf.format(date)
                    val split = str.split(",")
                    val day = split.get(0)
                    val dayNum = split.get(1)
                    val dayTime = split.get(3)
                    binding!!.tvDate.text = day + dayNum
                    binding!!.tvTime.text = dayTime
                    Handler(Looper.getMainLooper()).postDelayed(this,60000)
                }

            }
            Handler(Looper.getMainLooper()).post(runnable)
        }

    }

    override fun deleteItem(position: Int) {
        val getData = newList?.get(position)
        requireContext().removeWorkReqeust(newList?.get(position)?.randomString!!)
        val listRef =database.child("users").child(auth.currentUser?.uid!!).child("todoList")
        listRef.child(position.toString()).removeValue()
        mainFragmentViewModel.deleteItem(getData!!)

        val getUri = newList?.get(position)?.todoImage
        if(getUri != null){
            val contentResolver: ContentResolver = requireActivity().getContentResolver()
            contentResolver.delete(getUri.toUri(), null, null)
        }
        newList?.removeAt(position)
        todoAdapter.notifyItemRemoved(position)
        todoAdapter.notifyItemRangeChanged(position,newList!!.size)
        Toast.makeText(context,R.string.txt_delete_message, Toast.LENGTH_SHORT).show()
        if(newList?.size==0){
            binding!!.tvEmpty.visibility = View.VISIBLE
        }
        else{
            binding!!.tvEmpty.visibility = View.INVISIBLE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}