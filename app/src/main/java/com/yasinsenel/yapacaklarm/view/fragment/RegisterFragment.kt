package com.yasinsenel.yapacaklarm.view.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.yasinsenel.yapacaklarm.R
import com.yasinsenel.yapacaklarm.databinding.FragmentRegisterBinding
import com.yasinsenel.yapacaklarm.model.RegisterDataModel


class RegisterFragment() : Fragment(){

    private lateinit var auth : FirebaseAuth
    private lateinit var binding : FragmentRegisterBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        database = Firebase.database.reference

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnConfirm.setOnClickListener {
                writeNewUser(binding.edtUsername.text.toString(),binding.edtEmail.text.toString(),binding.edtPassword.text.toString())
               /* auth.createUserWithEmailAndPassword(binding.edtUsername.text.toString(),binding.edtPassword.text.toString())
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            val user = auth.currentUser
                            user?.sendEmailVerification()?.addOnCompleteListener {
                                if(it.isSuccessful){
                                    Toast.makeText(requireContext(),"E-Mail Doğrulayın.",Toast.LENGTH_SHORT).show()
                                    writeNewUser(binding.edtUsername.text.toString(),binding.edtEmail.text.toString(),binding.edtPassword.text.toString())
                                    val tabs = activity?.findViewById<ViewPager2>(R.id.viewPager)
                                    tabs?.currentItem = 0
                                }
                            }

                        }
                        else{
                            Toast.makeText(requireContext(),it.exception.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }*/
            }
        }

    }

    fun writeNewUser(name: String, email: String, password : String) {
        val user = RegisterDataModel(name,email)
        val key = database.child("posts").push().key!!
        database.child("users").child(key).setValue(user)
    }

}