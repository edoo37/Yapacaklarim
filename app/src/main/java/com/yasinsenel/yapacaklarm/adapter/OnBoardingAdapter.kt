package com.yasinsenel.yapacaklarm.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.yasinsenel.yapacaklarm.view.fragment.OnboardingOneFragment
import com.yasinsenel.yapacaklarm.view.fragment.OnboardingThreeFragment
import com.yasinsenel.yapacaklarm.view.fragment.OnboardingTwoFragment

class OnBoardingAdapter(fm : FragmentManager, var totalTabs : Int) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0->{
                OnboardingOneFragment()
            }
            1->{
                OnboardingTwoFragment()
            }
            2->{
                OnboardingThreeFragment()
            }
            else->{
                OnboardingOneFragment()
            }
        }
    }

}