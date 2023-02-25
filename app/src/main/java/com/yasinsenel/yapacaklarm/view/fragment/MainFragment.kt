package com.yasinsenel.yapacaklarm.view.fragment

import android.content.ContentResolver
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.orhanobut.hawk.Hawk
import com.yasinsenel.yapacaklarm.R
import com.yasinsenel.yapacaklarm.UserDocument
import com.yasinsenel.yapacaklarm.adapter.TodoAdapter
import com.yasinsenel.yapacaklarm.databinding.FragmentMainBinding
import com.yasinsenel.yapacaklarm.model.TodoData
import com.yasinsenel.yapacaklarm.utils.removeWorkReqeust
import com.yasinsenel.yapacaklarm.viewmodel.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class MainFragment : Fragment(), TodoAdapter.removeItem {
    private lateinit var binding : FragmentMainBinding
    private lateinit var todoAdapter : TodoAdapter
    private var setList : MutableList<TodoData>? = mutableListOf()
    private var newList : MutableList<TodoData>? = mutableListOf()
    private val filteredList : MutableList<TodoData> = mutableListOf()
    private val list : ArrayList<String> = arrayListOf()
    private val mainFragmentViewModel : MainFragmentViewModel by viewModels()
    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference
    private lateinit var db : FirebaseFirestore
    private lateinit var storage : StorageReference
    private lateinit var firebaseAnalytics : FirebaseAnalytics
    private var createStorageRef : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore
        database = Firebase.database.reference
        storage = Firebase.storage.reference
        firebaseAnalytics = Firebase.analytics
        MobileAds.initialize(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Hawk.init(requireContext()).build()

        checkAppMode()

        Firebase.crashlytics.setUserId(auth.currentUser!!.uid)
        setFrag(auth.currentUser!!.uid)


        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })


        val args = arguments
        val bundle = args?.getStringArray("sendUserData")
        binding!!.tvUsername.text = bundle?.get(0)

        todoAdapter = TodoAdapter(mainFragmentViewModel,this@MainFragment)
        getImageFromAPI()
        mainFragmentViewModel.getAllData(auth.currentUser!!.uid)
        mainFragmentViewModel.getRoomList.observe(viewLifecycleOwner) {
            it?.let {
                setList = it
            }
            if(it?.size!! == 0){
                db.collection("users").document(auth.currentUser?.uid!!).get()
                    .addOnSuccessListener {
                        val users: List<TodoData>? = it.toObject(UserDocument::class.java)?.todoList
                        setList = users?.toMutableList()
                        setAdapter(setList)
                    }
            }
            setList?.forEach {
                if(it.todoImage!=null){
                    createStorageRef = "images/${it.userId}/${it.todoName}/${it.todoImage?.split("/")?.last()}"
                    storage.child(createStorageRef!!).putFile(it.todoImage!!.toUri())
                }
                db.collection("users").document(auth.currentUser?.uid!!).update("todoList",FieldValue.arrayUnion(it))
            }
            setAdapter(setList)
        }


        binding.recyclerView.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener{
            override fun onChildViewAttachedToWindow(view: View) {
               //binding.tvEmpty.visibility = View.INVISIBLE
            }

            override fun onChildViewDetachedFromWindow(view: View) {
               //binding.tvEmpty.visibility = View.VISIBLE

            }

        })

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

        binding.fab.setOnClickListener {
            val user = auth.currentUser?.uid
            val bundle = Bundle()
            bundle.putString("userid",user)
            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_addTaskFragment,bundle)
        }


    }

    private fun setAdapter(setmyList : MutableList<TodoData>?){

        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            recyclerView.adapter = todoAdapter
            todoAdapter.setNewList(setmyList!!)
            todoAdapter.setData(setmyList)
            todoAdapter.setData(setmyList!!)

            if(setmyList.size>0){
                binding.tvEmpty.visibility = View.INVISIBLE
            }
            else{
                binding.tvEmpty.visibility = View.VISIBLE
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
                Glide.with(requireContext())
                    .load(it.urls.full)
                    .into(binding.ivMain)
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
                    binding.tvDate.text = day + dayNum
                    binding.tvTime.text = dayTime
                    Handler(Looper.getMainLooper()).postDelayed(this,60000)
                }

            }
            Handler(Looper.getMainLooper()).post(runnable)
        }

    }

    override fun deleteItem(data: Int, dataSize:Int) {
        val getData = setList?.get(data)
        requireContext().removeWorkReqeust(setList?.get(data)?.randomString!!)
        //val listRef =database.child("users").child(auth.currentUser?.uid!!).child("todoList")
        //listRef.child(position.toString()).removeValue()
        db.collection("users").document(auth.currentUser?.uid!!).update("todoList",FieldValue.arrayRemove(getData))
        mainFragmentViewModel.deleteItem(getData!!)
        if(getData.todoImage!=null){
            storage.child(createStorageRef!!).delete()
                .addOnSuccessListener {

                }
                .addOnFailureListener {

                }
        }
        val getUri = setList?.get(data)?.todoImage
        if(getUri != null){
            val contentResolver: ContentResolver = requireActivity().getContentResolver()
            contentResolver.delete(getUri.toUri(), null, null)
        }
        setList?.removeAt(data)
        todoAdapter.notifyItemRemoved(data)
        todoAdapter.notifyItemRangeChanged(data,dataSize)
        Toast.makeText(context,R.string.txt_delete_message, Toast.LENGTH_SHORT).show()
        if(setList?.size==0){
            binding.tvEmpty.visibility = View.VISIBLE
        }
        else{
            binding.tvEmpty.visibility = View.INVISIBLE
        }
    }

    fun setFrag(userId : String){
        storage.child("profile-images/${userId}")
            .downloadUrl
            .addOnSuccessListener {
                Glide.with(requireView())
                    .load(it)
                    .centerCrop()
                    .into(binding.ivUser)
            }
            .addOnFailureListener {

            }
    }



}