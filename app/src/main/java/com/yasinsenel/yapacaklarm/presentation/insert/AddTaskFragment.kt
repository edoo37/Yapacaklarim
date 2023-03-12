package com.yasinsenel.yapacaklarm.presentation.insert

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.shape.CornerFamily
import com.yasinsenel.yapacaklarm.R
import com.yasinsenel.yapacaklarm.analytics.AnalyticsTools
import com.yasinsenel.yapacaklarm.databinding.FragmentAddTaskBinding
import com.yasinsenel.yapacaklarm.model.TodoData
import com.yasinsenel.yapacaklarm.utils.createImageFile
import com.yasinsenel.yapacaklarm.utils.createWorkRequest
import com.yasinsenel.yapacaklarm.presentation.datepicker.DataPickerFragment
import com.yasinsenel.yapacaklarm.presentation.timepicker.TimePickerFragment
import com.yasinsenel.yapacaklarm.viewmodel.AddTaskFragmentViewModel
import com.yasinsenel.yapacaklarm.viewmodel.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class AddTaskFragment : Fragment() {
        private lateinit var binding : FragmentAddTaskBinding
        private var uri : Uri? = null
        lateinit var getContent : ActivityResultLauncher<Uri>
        private val addTaskFragmentViewModel : AddTaskFragmentViewModel by viewModels()
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

            binding.card.shapeAppearanceModel = binding.card.shapeAppearanceModel.toBuilder()
                .setBottomLeftCorner(CornerFamily.ROUNDED, 80f)
                .setBottomRightCorner(CornerFamily.ROUNDED, 80f)
                .build()

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

            binding.imageView.setOnClickListener {
                //readExternalPermissionContract.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                checkPermissionAndOpenCamera()
            }

            getContent  = registerForActivityResult(ActivityResultContracts.TakePicture()){
                if(it){
                    binding.imageView.setImageURI(uri)
                }
                else{
                    val contentResolver: ContentResolver = requireActivity().getContentResolver()
                    contentResolver.delete(uri!!, null, null)
                }
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
                    checkFields()
                }

                btnDate.setOnClickListener {
                    val datePickerFragment = DataPickerFragment()
                    val supportFragment = requireActivity().supportFragmentManager

                    supportFragment.setFragmentResultListener(
                        "REQUEST_KEY",
                        viewLifecycleOwner
                    ){ resultKey, bundle ->
                        if (resultKey == "REQUEST_KEY") {
                            val date = bundle.getString("SELECTED_DATE")
                            btnDate.setText(date)
                        }
                    }
                    datePickerFragment.show(supportFragment,"DatePickerFragment")
                }
                btnTime.setOnClickListener {
                    showTimePickerDialog()
                }
            }
    }

    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment { onTimeSelected(it) }
        timePicker.show(requireActivity().supportFragmentManager, "time")

    }

    private fun onTimeSelected(time: String) {
        binding.btnTime.setText(time)
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
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION)
        } else {
            invokeCamera()
        }
    }


    private fun checkFields(){
        binding.apply {
            if(tietTaskName.text!!.isEmpty() || tietTaskDesc.text!!.isEmpty() || btnDate.text.isEmpty()
                || btnTime.text.isEmpty()){
                Toast.makeText(requireContext(),R.string.txt_error_message,Toast.LENGTH_SHORT).show()
            }
            else{
                fillFields()
            }
        }

    }

    private fun fillFields(){
        binding.apply {
            val date = btnDate.text.toString()
            val time = btnTime.text.toString()
            val randomString = UUID.randomUUID().toString().substring(0,15)
            var uriString  : String? = null
            val args = arguments
            val userId = args?.getString("userid")
            if(uri != null){
                uriString = uri.toString()
            }
            val list = TodoData(tietTaskName.text.toString(),tietTaskDesc.text.toString(),date,time,uriString,randomString, userId)
            addTaskFragmentViewModel.addItem(list)
            addTaskFragmentViewModel.addTodoImageToFireStorage(list)


            // WORKMANAGER ISLEMLERI
            val splipt = date.split(".")
            val year = splipt.get(2).toInt()
            val month = splipt.get(1).toInt()  - 1
            val day = splipt.get(0).toInt()
            val split = time.split(":")
            val hour = split.get(0).toInt()
            val minute = split.get(1).toInt()

            val userSelectedDateTime = Calendar.getInstance()
            userSelectedDateTime.set(year,month,day,hour,minute)
            //WORK MANAGER
            val currentDateTime = Calendar.getInstance()
            val timee = userSelectedDateTime.timeInMillis/1000 - currentDateTime.timeInMillis/1000
            requireContext().createWorkRequest(tietTaskName.text.toString(),tietTaskName.text.toString(),timee,randomString)
            //ANALYTICS
            AnalyticsTools.logCustomEvent("eventTimes", bundleOf(userId.toString() to userSelectedDateTime.toString()))


            findNavController().popBackStack()
        }
    }

}