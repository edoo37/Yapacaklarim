package com.yasinsenel.yapacaklarm.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.yasinsenel.yapacaklarm.MainActivity
import com.yasinsenel.yapacaklarm.R
import com.yasinsenel.yapacaklarm.adapter.TablayoutAdapter
import com.yasinsenel.yapacaklarm.databinding.FragmentLoginRegisterBinding


class LoginRegisterFragment : Fragment() {
    private var binding : FragmentLoginRegisterBinding? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginRegisterBinding.inflate(inflater, container, false)
        return binding!!.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()


    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        /*if(currentUser != null){
            Navigation.findNavController(requireView()).navigate(R.id.action_loginRegisterFragment_to_mainFragment)
        }*/
    }

    fun setAdapter() {
        val tabArrayList = arrayOf(getString(R.string.txt_tblayout_login), getString(R.string.txt_tblayout_register))
        val adapter = TablayoutAdapter(requireActivity(), tabArrayList.size)
        binding!!.apply {
            viewPager.adapter = adapter

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabArrayList[position]
            }.attach()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}