package com.yasinsenel.yapacaklarm

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import koleton.SkeletonLoader
import koleton.SkeletonLoaderFactory

@HiltAndroidApp
class MyApplication : Application(), SkeletonLoaderFactory {
    override fun newSkeletonLoader(): SkeletonLoader {
        return SkeletonLoader.Builder(this)
            .color(R.color.skeleton_color)
            .cornerRadius(8.0f)
            .build()
    }
}