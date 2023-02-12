package com.yasinsenel.yapacaklarm.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.yasinsenel.yapacaklarm.adapter.TablayoutAdapter
import com.yasinsenel.yapacaklarm.databinding.FragmentLoginRegisterBinding


class LoginRegisterFragment : Fragment() {
    private lateinit var binding : FragmentLoginRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginRegisterBinding.inflate(inflater,container,false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabArrayList = arrayOf("Login","Register")
        val adapter = TablayoutAdapter(requireActivity(),tabArrayList.size)
        binding.apply {
            viewPager.adapter = adapter

            TabLayoutMediator(tabLayout,viewPager){tab,position->
                tab.text = tabArrayList[position]
            }.attach()
        }

    }

}