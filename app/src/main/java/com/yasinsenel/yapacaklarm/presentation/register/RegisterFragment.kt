package com.yasinsenel.yapacaklarm.presentation.register



import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.yasinsenel.yapacaklarm.R
import com.yasinsenel.yapacaklarm.databinding.FragmentRegisterBinding
import com.yasinsenel.yapacaklarm.data.model.RegisterDataModel
import com.yasinsenel.yapacaklarm.viewmodel.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject



@AndroidEntryPoint
class RegisterFragment() : Fragment(){

    private lateinit var auth : FirebaseAuth
    private var binding : FragmentRegisterBinding? = null
    private var uri : Uri? = null
    private var isReadPermission = false
    private var permissionLauncher: ActivityResultLauncher<Array<String>>? = null
    private val mainFragmentViewModel : MainFragmentViewModel by viewModels()

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var firebaseStorage: StorageReference
    @Inject
    lateinit var firebaseDatabase: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
                result->
            isReadPermission = result[android.Manifest.permission.READ_EXTERNAL_STORAGE] ?: isReadPermission
        }



        binding!!.apply {
            btnConfirm.setOnClickListener {
                firebaseAuth.createUserWithEmailAndPassword(binding!!.tietEmail.text.toString(),binding!!.tietPassword.text.toString())
                    .addOnCompleteListener {
                        progressBar.isVisible = true
                        if(it.isSuccessful){
                            writeUserToFiresstore(firebaseAuth.currentUser!!.uid,binding!!.tietUsername.text.toString(),binding!!.tietEmail.text.toString(),binding!!.tietPassword.text.toString(),requireActivity())
                            //writeNewUser(userId,binding!!.edtUsername.text.toString(),binding!!.edtEmail.text.toString(),binding!!.edtPassword.text.toString())
                            if(uri!=null){
                                firebaseStorage.child("profile-images/${firebaseAuth.currentUser!!.uid}").putFile(uri!!)
                            }
                            progressBar.isVisible = false
                        }
                        else{
                            Toast.makeText(requireContext(),it.exception.toString(),Toast.LENGTH_SHORT).show()
                            progressBar.isVisible = false
                        }
                    }
            }

            ivProfileImage.setOnClickListener {
                checkPermission()
                if(isReadPermission)
                    imageLauncher.launch("image/*")

            }


        }

    }
    private val imageLauncher = registerForActivityResult(ActivityResultContracts.GetContent(),
        ActivityResultCallback {
            uri = it
            binding?.ivProfileImage?.setImageURI(uri)
        })
    fun writeNewUser(userId:String,name: String, email: String, password : String) {
        val user = RegisterDataModel(name,email,password)
        val dbRef = firebaseDatabase.reference.child("users").child(userId)
        dbRef.orderByValue().equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.value != null){
                        Toast.makeText(requireContext(),R.string.txt_registered_user,Toast.LENGTH_SHORT).show()
                    }
                    else{
                        dbRef.setValue(user)
                        val tabs = activity?.findViewById<ViewPager2>(R.id.viewPager)
                        tabs?.currentItem = 0
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
    fun writeUserToFiresstore(userId:String,name: String, email: String, password : String,activity : FragmentActivity){
        mainFragmentViewModel.saveUserDatatoFirestore(userId,name,email,password,activity)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun checkPermission(){
        isReadPermission = ContextCompat.checkSelfPermission(requireContext(),
            android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED

        val permissionRequest : MutableList<String> = mutableListOf()
        if(!isReadPermission){
            permissionRequest.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if(permissionRequest.isNotEmpty()){
            permissionLauncher?.launch(permissionRequest.toTypedArray())
        }
    }
}