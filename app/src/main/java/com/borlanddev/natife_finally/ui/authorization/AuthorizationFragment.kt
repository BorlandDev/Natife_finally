package com.borlanddev.natife_finally.ui.authorization

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.borlanddev.natife_finally.R
import com.borlanddev.natife_finally.databinding.FragmentAuthorizationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthorizationFragment(): Fragment(R.layout.fragment_authorization) {

    private var binding: FragmentAuthorizationBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAuthorizationBinding.bind(view)

    }
}