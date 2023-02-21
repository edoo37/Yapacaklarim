package com.yasinsenel.yapacaklarm.analytics

import android.os.Bundle
import androidx.core.os.bundleOf
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

class AnalyticsTools {

    private val firebaseAnalytics = Firebase.analytics

    companion object {
        val analyticsTools = AnalyticsTools()
        fun logCustomEvent (screenName: String, extras: Bundle? = null) {
            analyticsTools.logCustomEvent(screenName, extras)
        }
    }

    private fun logCustomEvent(eventName: String, extras: Bundle? = null) {
        val bundle = bundleOf().apply {
            extras?.let {
                putAll(it)
            }
        }
        firebaseAnalytics.logEvent(eventName, bundle)
    }
}