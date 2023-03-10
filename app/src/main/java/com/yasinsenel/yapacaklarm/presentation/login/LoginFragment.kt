package com.yasinsenel.yapacaklarm.presentation.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.yasinsenel.yapacaklarm.R
import com.yasinsenel.yapacaklarm.data.model.User
import com.yasinsenel.yapacaklarm.databinding.FragmentLoginBinding
import com.yasinsenel.yapacaklarm.utils.Resource
import com.yasinsenel.yapacaklarm.viewmodel.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var binding : FragmentLoginBinding? = null
    private val mainFragmentViewModel : MainFragmentViewModel by viewModels()
    private var getList : User? = null
    @Inject
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                progressBar.isVisible = true
                //loginControl(binding.edtEmail.text.toString())
                auth.signInWithEmailAndPassword(binding!!.tietEmail.text.toString(),binding!!.tietPassword.text.toString()).addOnCompleteListener {
                    if(it.isSuccessful){
                    progressBar.isVisible = false
                    sendData(view)
                    }
                }
            }
        }
    }

    /*fun loginSendData(userId : String){
        val reference = database.child("users").child(userId)
        reference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value != null){
                    //val usernameFromDb = snapshot.child("username").value
                    //val emailFromDb = snapshot.child("email").value
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
                    /*val bundle = Bundle()
                    val arrayList = arrayOf(usernameFromDb.toString(),userId)
                    bundle.putStringArray("sendUserData",arrayList)
                    Navigation.findNavController(requireView()).navigate(R.id.action_loginRegisterFragment_to_mainFragment)*/
                }

            }

            override fun onCancelled(error: DatabaseError) {
                println(error)
            }

        })

    }*/
    fun sendData(view : View){
        mainFragmentViewModel.getUserDataFromFirestore(view)
        viewLifecycleOwner.lifecycleScope.launch {
                mainFragmentViewModel.getUserDataFromFirestoree.collect{
                    it?.let {todoList->
                        when(todoList){
                            is Resource.Success->{
                                getList = todoList.data!!
                                val bundle = Bundle()
                                bundle.putParcelable("sendUserData",getList)
                                Navigation.findNavController(requireView()).navigate(R.id.action_loginRegisterFragment_to_mainFragment,bundle)
                            }
                            is Resource.Loading->{}
                            is Resource.Error->{}
                        }
                    }
                }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}