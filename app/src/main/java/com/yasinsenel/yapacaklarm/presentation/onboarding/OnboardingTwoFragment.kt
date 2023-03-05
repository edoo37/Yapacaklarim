package com.yasinsenel.yapacaklarm.presentation.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.yasinsenel.yapacaklarm.R
import com.yasinsenel.yapacaklarm.databinding.FragmentOnboardingTwoBinding


class OnboardingTwoFragment : Fragment() {

    private lateinit var binding : FragmentOnboardingTwoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingTwoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnNext.setOnClickListener {
                val tabs = activity?.findViewById<ViewPager>(R.id.viewPager)
                tabs?.currentItem = 2
            }
            btnBack.setOnClickListener {
                val tabs = activity?.findViewById<ViewPager>(R.id.viewPager)
                tabs?.currentItem = 0
            }
        }
    }

}