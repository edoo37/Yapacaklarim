package com.yasinsenel.yapacaklarm.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager
import com.orhanobut.hawk.Hawk
import com.yasinsenel.yapacaklarm.R
import com.yasinsenel.yapacaklarm.databinding.FragmentOnboardingThreeBinding


class OnboardingThreeFragment : Fragment() {
    private lateinit var binding : FragmentOnboardingThreeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingThreeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnFinish.setOnClickListener {
                finishOnboarding()
                Navigation.findNavController(requireView()).navigate(R.id.action_onboardingFragment_to_loginRegisterFragment)
            }
            btnBack.setOnClickListener {
                val tabs = activity?.findViewById<ViewPager>(R.id.viewPager)
                tabs?.currentItem = 1
            }
        }
    }

    private fun finishOnboarding(){
        Hawk.put("finishOb",true)
    }

}