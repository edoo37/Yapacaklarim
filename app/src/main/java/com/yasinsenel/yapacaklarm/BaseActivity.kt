package com.yasinsenel.yapacaklarm

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.preference.PreferenceManager
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AppCompatActivity
import com.yasinsenel.yapacaklarm.utils.ContextUtils
import com.yasinsenel.yapacaklarm.utils.ContextUtils.Companion.updateLocale
import java.util.*

open class BaseActivity: AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
// get chosen language from shread preference
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase)
        super.attachBaseContext(localeUpdatedContext)
    }

}