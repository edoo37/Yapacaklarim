package com.yasinsenel.yapacaklarm

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import com.orhanobut.hawk.Hawk
import com.yasinsenel.yapacaklarm.databinding.ActivityMainBinding
import java.util.*

class MainActivity : BaseActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Hawk.init(this).build()

        val selectedLanguage = Hawk.get("selectedLanguage","")
        when(selectedLanguage){
            "English"->loadLocale("en")
            "Türkçe"->loadLocale("tr")
        }

    }

    private fun loadLocale(lang:String){
        val locale= Locale(lang)
        //Seçilen dilimizi bir sonraki girişlerde default dil haline getiriyoruz.
        Locale.setDefault(locale)
        val config: Configuration = Configuration()
        config.setLocale(locale)
        this.resources.updateConfiguration(config,this.resources.displayMetrics)
    }
}