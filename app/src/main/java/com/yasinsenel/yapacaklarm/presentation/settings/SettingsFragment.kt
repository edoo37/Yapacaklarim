package com.yasinsenel.yapacaklarm.presentation.settings

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.orhanobut.hawk.Hawk
import com.yasinsenel.yapacaklarm.R
import com.yasinsenel.yapacaklarm.databinding.FragmentSettingsBinding
import java.util.*


class SettingsFragment : Fragment() {

    private lateinit var binding : FragmentSettingsBinding
    private val arrItems = arrayOf("Türkçe","English")


    var selectedItemIndex = 0
    var selectedItem = arrItems[selectedItemIndex]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.button.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                println("Dark")
                Hawk.put("mode",true)
                Hawk.put("isSelected",true)
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                println("Light")
                Hawk.put("mode",false)
                Hawk.put("isSelected",true)
            }
        }

        loadDatas()
        selectLanguage()
    }

    fun selectLanguage(){
        binding.tvSelectLanguage.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.txt_title)
                .setSingleChoiceItems(arrItems,selectedItemIndex){dialog,which->
                    selectedItemIndex = which
                    selectedItem = arrItems[which]
                }
                .setPositiveButton(R.string.btn_positive){dialog,which->
                    when(selectedItemIndex){
                        0->{setLocale("tr")
                            Hawk.put("selectedLang","tr")}
                        1->{setLocale("en")
                            Hawk.put("selectedLang","en")}
                    }
                    binding.tvSelectLanguage.setText(selectedItem)
                    Hawk.put("selectedLanguage",selectedItem)
                }
                .setNegativeButton(R.string.btn_negative){dialog,which->

                }.show()
        }
    }

    private fun loadDatas(){
        val value = Hawk.get("mode",false)
        val selectedLanguage = Hawk.get("selectedLanguage","Türkçe")
        println(selectedLanguage)
        if(value){
            binding.button.isChecked = true
        }
        binding.tvSelectLanguage.setText(selectedLanguage)
    }
    private fun setLocale(lang: String) {
        val locale= Locale(lang)
        Locale.setDefault(locale)
        val config:Configuration= Configuration()
        config.setLocale(locale)
        requireContext().resources.updateConfiguration(config,requireContext().resources.displayMetrics)
        Navigation.findNavController(requireView()).navigate(R.id.action_settingsFragment_self)
    }

}