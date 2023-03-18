package com.yasinsenel.yapacaklarm.presentation.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yasinsenel.yapacaklarm.databinding.FragmentOnboardingBinding


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