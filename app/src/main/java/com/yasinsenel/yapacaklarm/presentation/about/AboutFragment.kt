package com.yasinsenel.yapacaklarm.presentation.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.shape.CornerFamily
import com.yasinsenel.yapacaklarm.R
import com.yasinsenel.yapacaklarm.databinding.FragmentAboutBinding


class AboutFragment : Fragment() {

    private lateinit var binding : FragmentAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.card.shapeAppearanceModel = binding.card.shapeAppearanceModel.toBuilder()
            .setBottomLeftCorner(CornerFamily.ROUNDED, 80f)
            .setBottomRightCorner(CornerFamily.ROUNDED, 80f)
            .build()
    }

}