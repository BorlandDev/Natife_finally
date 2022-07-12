package com.borlanddev.natife_finally.ui.list_users

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.borlanddev.natife_finally.R
import com.borlanddev.natife_finally.databinding.FragmentListUsersBinding
import com.borlanddev.natife_finally.helpers.APP_PREFERENCES
import com.borlanddev.natife_finally.helpers.Prefs
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListUsersFragment(): Fragment(R.layout.fragment_list_users) {

    private var binding: FragmentListUsersBinding? = null
    @Inject
    lateinit var prefs: Prefs

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListUsersBinding.bind(view)

        binding?.button2?.setOnClickListener {
            prefs.preferences.edit().putString(APP_PREFERENCES, "").apply()

            findNavController().navigate(R.id.authorizationFragment)
        }

    }
}