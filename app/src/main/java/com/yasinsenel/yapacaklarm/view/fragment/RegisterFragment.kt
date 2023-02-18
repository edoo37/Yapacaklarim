package com.yasinsenel.yapacaklarm.view.fragment


import android.app.Activity.RESULT_OK
import android.content.Intent
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
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.yasinsenel.yapacaklarm.R
import com.yasinsenel.yapacaklarm.databinding.FragmentRegisterBinding
import com.yasinsenel.yapacaklarm.model.RegisterDataModel


class RegisterFragment() : Fragment(){

    private lateinit var auth : FirebaseAuth
    private var binding : FragmentRegisterBinding? = null
    private lateinit var database: DatabaseReference
    private lateinit var db : FirebaseFirestore
    private lateinit var storage: StorageReference
    private var uri : Uri? = null
    private var isReadPermission = false
    private var permissionLauncher: ActivityResultLauncher<Array<String>>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        database = Firebase.database.reference
        db = Firebase.firestore
        storage = Firebase.storage.reference

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
                auth.createUserWithEmailAndPassword(binding!!.edtEmail.text.toString(),binding!!.edtPassword.text.toString())
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            val user = auth.currentUser
                            val userId = user?.uid!!
                            writeUserToFiresstore(userId,binding!!.edtUsername.text.toString(),binding!!.edtEmail.text.toString(),binding!!.edtPassword.text.toString())
                            //writeNewUser(userId,binding!!.edtUsername.text.toString(),binding!!.edtEmail.text.toString(),binding!!.edtPassword.text.toString())
                            if(uri!=null){
                                storage.child("profile-images/${userId}").putFile(uri!!)
                            }
                        }
                        else{
                            Toast.makeText(requireContext(),it.exception.toString(),Toast.LENGTH_SHORT).show()
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
        val dbRef = database.child("users").child(userId)
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
    fun writeUserToFiresstore(userId:String,name: String, email: String, password : String){
        val user = hashMapOf(
            "userId" to userId,
            "name" to name,
            "email" to email,
            "password" to password
        )
        db.collection("users")
            .document(userId)
            .set(user)
            .addOnSuccessListener {
                val tabs = activity?.findViewById<ViewPager2>(R.id.viewPager)
                tabs?.currentItem = 0
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(),it.toString(),Toast.LENGTH_SHORT).show()
            }
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