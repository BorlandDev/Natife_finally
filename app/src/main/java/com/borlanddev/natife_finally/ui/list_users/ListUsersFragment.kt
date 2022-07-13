package com.borlanddev.natife_finally.ui.list_users

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.borlanddev.natife_finally.R
import com.borlanddev.natife_finally.databinding.FragmentListUsersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListUsersFragment : Fragment(R.layout.fragment_list_users) {

    private var binding: FragmentListUsersBinding? = null
    private val listUsersVM: ListUsersVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListUsersBinding.bind(view)

        binding?.LogOutButton?.setOnClickListener {
            listUsersVM.logOut()
            findNavController().navigate(R.id.action_listUsersFragment_to_authorizationFragment)
        }
    }
}