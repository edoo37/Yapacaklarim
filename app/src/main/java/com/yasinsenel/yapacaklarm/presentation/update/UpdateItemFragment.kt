package com.yasinsenel.yapacaklarm.presentation.update

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yasinsenel.yapacaklarm.databinding.FragmentUpdateItemBinding
import com.yasinsenel.yapacaklarm.model.TodoData
import com.yasinsenel.yapacaklarm.presentation.insert.AddTaskFragment
import com.yasinsenel.yapacaklarm.utils.createImageFile
import com.yasinsenel.yapacaklarm.utils.createWorkRequest
import com.yasinsenel.yapacaklarm.utils.removeWorkReqeust
import com.yasinsenel.yapacaklarm.presentation.datepicker.DataPickerFragment
import com.yasinsenel.yapacaklarm.presentation.timepicker.TimePickerFragment
import com.yasinsenel.yapacaklarm.viewmodel.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class UpdateItemFragment : Fragment() {

    private lateinit var binding : FragmentUpdateItemBinding
    private var todoModel: TodoData? = TodoData()
    private val mainFragmentViewModel : MainFragmentViewModel by viewModels()
    private var uri : Uri? = null
    private lateinit var db : FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    lateinit var getContent : ActivityResultLauncher<Uri>
    private var getList : MutableList<TodoData> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Firebase.firestore
        auth = Firebase.auth

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
        binding.apply {
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    if(uri != null){
                        val contentResolver: ContentResolver = requireActivity().getContentResolver()
                        contentResolver.delete(uri!!, null, null)
                        findNavController().popBackStack()
                    }
                    findNavController().popBackStack()
                }

            })

            getContent  = registerForActivityResult(ActivityResultContracts.TakePicture()){
                if(it){
                    imageView.setImageURI(uri)
                }
                else{
                    val contentResolver: ContentResolver = requireActivity().getContentResolver()
                    contentResolver.delete(uri!!, null, null)
                }
            }

            imageView.setOnClickListener {
                //readExternalPermissionContract.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                checkPermissionAndOpenCamera()
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
                        edtDate.setText(date)
                    }
                }
                datePickerFragment.show(supportFragment,"DatePickerFragment")
            }
            edtTime.setOnClickListener {
                showTimePickerDialog()
            }
        }


        val bundleData = arguments
        todoModel = bundleData?.getParcelable("dataClass")
        val getid = todoModel?.id
        binding.edtTaskName.setText(todoModel?.todoName)
        binding.edtTaskDesc.setText(todoModel?.todoDesc)
        binding.edtDate.setText(todoModel?.todoDate)
        binding.edtTime.setText(todoModel?.todoTime)
        binding.imageView.setImageURI(todoModel?.todoImage?.toUri())

        val getString = todoModel?.randomString
        val getUri = todoModel?.todoImage
        val getUserId = todoModel?.userId

        mainFragmentViewModel.getAllTodoDataFromFirestore(todoModel!!)
        mainFragmentViewModel.getFirebaseList.observe(viewLifecycleOwner){
            it?.let {
                getList = it
            }
        }

        binding.btnConfirm.setOnClickListener {
            getList.forEach {
                if(it.id==todoModel!!.id){
                    db.collection("users").document(auth.currentUser!!.uid).update("todoList", FieldValue.arrayRemove(it))
                    println(it)
                }
            }
            val date = binding.edtDate.text.toString()
            val time = binding.edtTime.text.toString()
            val splipt = date.split(".")
            val year = splipt.get(2).toInt()
            val month = splipt.get(1).toInt()  - 1
            val day = splipt.get(0).toInt()
            val split = time.split(":")
            val hour = split.get(0).toInt()
            val minute = split.get(1).toInt()
            var uriString  : String? = null
            if(uri != null){
                uriString = uri.toString()
            }
            else{
                uriString = getUri
            }
            val userSelectedDateTime = Calendar.getInstance()
            userSelectedDateTime.set(year,month,day,hour,minute)
            val currentDateTime = Calendar.getInstance()
            val timee = userSelectedDateTime.timeInMillis/1000 - currentDateTime.timeInMillis/1000
            val randomString = UUID.randomUUID().toString().substring(0,15)
            val args = arguments
            val list = TodoData(binding.edtTaskName.text.toString(),binding.edtTaskDesc.text.toString(),date,time,uriString,randomString, getUserId,getid)
            mainFragmentViewModel.updateItem(list)
            requireContext().removeWorkReqeust(getString!!)
            requireContext().createWorkRequest(binding.edtTaskName.text.toString(),binding.edtTaskDesc.text.toString(),timee,randomString)
            findNavController().popBackStack()
        }


    }


    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment { onTimeSelected(it) }
        timePicker.show(requireActivity().supportFragmentManager, "time")

    }

    private fun onTimeSelected(time: String) {
        binding.edtTime.setText(time)
    }

    fun invokeCamera(){
        val file = requireActivity().createImageFile()

        try{
            uri = FileProvider.getUriForFile(requireContext(),"com.yasinsenel.yapacaklarm.fileprovider",file)
        }
        catch (e : Exception){

        }
        getContent.launch(uri)

    }



    fun checkPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(requireActivity().applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA),
                AddTaskFragment.CAMERA_PERMISSION
            )
        } else {
            invokeCamera()
        }
    }

}