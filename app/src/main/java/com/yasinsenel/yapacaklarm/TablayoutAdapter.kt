package com.yasinsenel.yapacaklarm

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yasinsenel.yapacaklarm.presentation.login.LoginFragment
import com.yasinsenel.yapacaklarm.presentation.register.RegisterFragment

class TablayoutAdapter(fm : FragmentActivity, var totalTabs : Int) : FragmentStateAdapter(fm) {
    override fun getItemCount(): Int {
        return totalTabs
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                LoginFragment()
            }
            1->{
                RegisterFragment()
            }
            else->{
                RegisterFragment()
            }
        }
    }
}