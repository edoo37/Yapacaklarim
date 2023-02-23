package com.yasinsenel.yapacaklarm.view.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.orhanobut.hawk.Hawk
import com.yasinsenel.yapacaklarm.R
import com.yasinsenel.yapacaklarm.databinding.FragmentSplashBinding


class SplashFragment : Fragment() {
    private lateinit var binding : FragmentSplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Hawk.init(requireContext()).build()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        checkSplashCompleted()
        binding = FragmentSplashBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
    private fun checkSplashCompleted(){
        val getHawkSplashData = Hawk.get("finishOb",false)
        if(getHawkSplashData){
            Navigation.findNavController(requireView()).navigate(R.id.action_splashFragment_to_loginRegisterFragment)
        }
        else{
            Handler(Looper.getMainLooper()!!).postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_onboardingFragment)
            },2300)
        }
    }
}