package com.yasinsenel.yapacaklarm.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.yasinsenel.yapacaklarm.R
import com.yasinsenel.yapacaklarm.databinding.FragmentLoginBinding
import com.yasinsenel.yapacaklarm.model.RegisterDataModel


class LoginFragment : Fragment() {
    private var binding : FragmentLoginBinding? = null
    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        database = Firebase.database.reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.apply {
            btnConfirm.setOnClickListener {
                //loginControl(binding.edtEmail.text.toString())
                auth.signInWithEmailAndPassword(binding!!.edtEmail.text.toString(),binding!!.edtPassword.text.toString()).addOnCompleteListener {
                    if(it.isSuccessful){
                        val user = auth.currentUser?.uid
                        loginSendData(user!!)
                    }
                }
            }
        }
    }

    fun loginSendData(userId : String){
        val reference = database.child("users").child(userId)
        var arrayList : Array<String> = arrayOf()
        reference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value != null){
                    val usernameFromDb = snapshot.child("username").value
                    val emailFromDb = snapshot.child("email").value
                   /*if(binding!!.edtPassword.text.toString() == passwordFromDb.toString())
                    {
                        val bundle = Bundle()
                        val loginArray = arrayOf(usernameFromDb.toString(),email)
                        bundle.putStringArray("bundle",loginArray)
                        Navigation.findNavController(requireView()).navigate(R.id.action_loginRegisterFragment_to_mainFragment,bundle)
                    }
                    else{
                        Toast.makeText(requireContext(),R.string.txt_wrong_password,Toast.LENGTH_SHORT).show()
                    }*/
                    val bundle = Bundle()
                    arrayList = arrayOf(usernameFromDb.toString(),userId)
                    bundle.putStringArray("sendUserData",arrayList)
                    Navigation.findNavController(requireView()).navigate(R.id.action_loginRegisterFragment_to_mainFragment,bundle)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                println(error)
            }

        })

    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}