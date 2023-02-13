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
import com.google.firebase.ktx.Firebase
import com.yasinsenel.yapacaklarm.R
import com.yasinsenel.yapacaklarm.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    private lateinit var binding : FragmentLoginBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnConfirm.setOnClickListener {
                auth.signInWithEmailAndPassword(binding.edtUsername.text.toString(),binding.edtPassword.text.toString()).addOnCompleteListener {
                    if(it.isSuccessful){
                        val user = auth.currentUser
                        if(user!!.isEmailVerified){
                            Navigation.findNavController(requireView()).navigate(R.id.action_loginRegisterFragment_to_mainFragment)
                        }
                        else{
                            Toast.makeText(requireContext(),"E-mail'i doğrulayın.",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}