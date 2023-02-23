package com.yasinsenel.yapacaklarm.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.yasinsenel.yapacaklarm.R
import com.yasinsenel.yapacaklarm.adapter.OnBoardingAdapter
import com.yasinsenel.yapacaklarm.databinding.FragmentOnboardingBinding
import kotlinx.coroutines.flow.combine


class OnboardingFragment : Fragment() {
    private lateinit var binding : FragmentOnboardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnboardingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply{
            val tabArrayList = arrayOf("Asd","bcd","adsdas")
            viewPager.adapter = OnBoardingAdapter(childFragmentManager,tabArrayList.size)
            tabLayout.tag = tabArrayList
            tabLayout.setupWithViewPager(viewPager)
        }
    }

}