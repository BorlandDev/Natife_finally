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

        val userAdapter = UserAdapter(onItemClick = {
            findNavController().navigate(R.id.action_listUsersFragment_to_chatFragment)
        })

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            listUsersVM.listUsersVM.collect {
                userAdapter.submitList(it)
            }
        }

        binding?.apply {
            progressBar.visibility = View.INVISIBLE
            LogOutButton.isEnabled = true

            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = userAdapter

            swipeRefreshLayout.setOnRefreshListener {
                listUsersVM.getUsers()
                swipeRefreshLayout.isRefreshing = false
            }

            LogOutButton.setOnClickListener {
                listUsersVM.logOut()
                progressBar.visibility = View.VISIBLE
                LogOutButton.isEnabled = false

                findNavController().navigate(R.id.action_listUsersFragment_to_authorizationFragment)
            }
        }

    }
}
