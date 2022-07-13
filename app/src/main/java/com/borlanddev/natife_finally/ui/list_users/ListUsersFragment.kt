package com.borlanddev.natife_finally.ui.list_users

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.borlanddev.natife_finally.R
import com.borlanddev.natife_finally.adapter.UserAdapter
import com.borlanddev.natife_finally.databinding.FragmentListUsersBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListUsersFragment : Fragment(R.layout.fragment_list_users) {

    private var binding: FragmentListUsersBinding? = null
    private val listUsersVM: ListUsersVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListUsersBinding.bind(view)

        binding?.also {
            it.progressBar.visibility = View.INVISIBLE
            it.LogOutButton.isEnabled = true
        }

        val userAdapter = UserAdapter(onItemClick = {
            findNavController().navigate(R.id.action_listUsersFragment_to_chatFragment)
        })

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            listUsersVM.listUsers.collect {
                userAdapter.submitList(it.toList())
            }
        }

        binding?.apply {
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = userAdapter
        }

        binding?.LogOutButton?.setOnClickListener {
            listUsersVM.logOut()

            binding?.also {
                it.progressBar.visibility = View.VISIBLE
                it.LogOutButton.isEnabled = false
            }
            findNavController().navigate(R.id.action_listUsersFragment_to_authorizationFragment)
        }


    }
}