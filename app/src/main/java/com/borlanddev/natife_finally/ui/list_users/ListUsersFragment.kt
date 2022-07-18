package com.borlanddev.natife_finally.ui.list_users

import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.borlanddev.natife_finally.R
import com.borlanddev.natife_finally.adapters.UserAdapter
import com.borlanddev.natife_finally.databinding.FragmentListUsersBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListUsersFragment : Fragment(R.layout.fragment_list_users) {

    private var binding: FragmentListUsersBinding? = null
    private val listUsersVM: ListUsersVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListUsersBinding.bind(view)

        val userAdapter = UserAdapter(onItemClick = {
            val direction = ListUsersFragmentDirections.actionListUsersFragmentToChatFragment(it.id)
            findNavController().navigate(direction)
        })

        lifecycleScope.launch {
            listUsersVM.users.collect {
                userAdapter.submitList(it)
            }
        }

        binding?.apply {
            progressBar.isInvisible = true
            logOutButton.isEnabled = true

            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = userAdapter

            swipeRefreshLayout.setOnRefreshListener {
                listUsersVM.getUsers()
                swipeRefreshLayout.isRefreshing = false
            }

            logOutButton.setOnClickListener {
                listUsersVM.logOut()
                progressBar.isVisible = true
                logOutButton.isEnabled = false

                findNavController().navigate(R.id.action_listUsersFragment_to_authorizationFragment)
            }
        }
    }
}
