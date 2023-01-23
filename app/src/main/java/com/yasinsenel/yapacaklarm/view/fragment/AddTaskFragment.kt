package com.yasinsenel.yapacaklarm.view.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.orhanobut.hawk.Hawk
import com.yasinsenel.yapacaklarm.R
import com.yasinsenel.yapacaklarm.databinding.FragmentAddTaskBinding
import com.yasinsenel.yapacaklarm.model.TodoData
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddTaskFragment : Fragment() {
        private lateinit var binding : FragmentAddTaskBinding
        private var uri : Uri? = null
        lateinit var getContent : ActivityResultLauncher<Uri>
        private var todoModel: TodoData? = TodoData()
        companion object{
            const val CAMERA_PERMISSION = 1
        }
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
            getContent  = registerForActivityResult(ActivityResultContracts.TakePicture()){
                if(it){
                    binding.imageView.setImageURI(uri)
                }
                else{

                }
            }

            val bundleData = arguments
            todoModel = bundleData?.getParcelable("dataClass")
            binding.edtTaskName.setText(todoModel?.todoName)
            binding.edtTaskDesc.setText(todoModel?.todoDesc)
            binding.edtDate.setText(todoModel?.todoDate)
            binding.imageView.setImageURI(todoModel?.todoImage?.toUri())


        binding.imageView.setOnClickListener {
            //readExternalPermissionContract.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            checkPermissionAndOpenCamera()
        }

        val readExternalPermissionContract =  registerForActivityResult(ActivityResultContracts.RequestPermission()) { isPermissionAccepted ->
            if(isPermissionAccepted) {
                Toast.makeText(requireContext(), "Permission is accepted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Permission is declined", Toast.LENGTH_SHORT).show()
            }
        }



        binding.apply {
            btnConfirm.setOnClickListener {
                val addList : ArrayList<TodoData> = Hawk.get("myData", arrayListOf())
                val date = binding.edtDate.text.toString()
                val time = binding.edtTime.text.toString()
                val uriString = uri.toString()
                println(addList)
                addList.add(TodoData(binding.edtTaskName.text.toString(),date,time,uriString))
                Hawk.put("myData",addList)
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
            edtTime.setOnClickListener {
                showTimePickerDialog()
            }

        }
    }

    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment { onTimeSelected(it) }
        timePicker.show(requireActivity().supportFragmentManager, "time")

    }

    private fun onTimeSelected(time: String) {
        binding.edtTime.setText(time)
    }

    lateinit var currentPhotoPath: String
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    fun invokeCamera(){
        val file = createImageFile()

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
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION)
        } else {
            invokeCamera()
        }
    }

}