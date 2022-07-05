package com.borlanddev.natife_finally.ui.list_users

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.borlanddev.natife_finally.R
import com.borlanddev.natife_finally.databinding.FragmentListUsersBinding

class ListUsersFragment(): Fragment(R.layout.fragment_list_users) {

    private var binding: FragmentListUsersBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListUsersBinding.bind(view)
    }
}