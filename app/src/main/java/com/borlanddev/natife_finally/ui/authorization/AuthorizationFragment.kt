package com.borlanddev.natife_finally.ui.authorization

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.borlanddev.natife_finally.R
import com.borlanddev.natife_finally.databinding.FragmentAuthorizationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthorizationFragment : Fragment(R.layout.fragment_authorization) {

    private var binding: FragmentAuthorizationBinding? = null
    private val authorizationVM: AuthorizationVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAuthorizationBinding.bind(view)

        if (authorizationVM.isSignedIn()) {
            authorizationVM.authorization(authorizationVM.getSavedName())
            waitingForConnection()
        }

        binding?.apply {
            signUpButton.setOnClickListener {
                val username = singInTextInput.text.toString()
                if (username.isEmpty()) {
                    val toast = Toast.makeText(
                        requireContext(),
                        R.string.wrong_authorization,
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else {
                    authorizationVM.authorization(username)
                    waitingForConnection()
                }
            }
        }
    }

    private fun waitingForConnection() {
        binding?.apply {
            progressBar.isVisible = true
            signUpButton.isEnabled = false
            singInTextInput.isEnabled = false
        }

        viewLifecycleOwner.lifecycleScope.launch {
            authorizationVM.singedIn.collect {
                if (it) {
                    goToListUsers()
                }
            }
        }
    }

    private fun goToListUsers() {
        binding?.apply {
            progressBar.isInvisible = true
            signUpButton.isEnabled = true
            singInTextInput.isEnabled = true
        }
        findNavController().navigate(R.id.action_authorizationFragment_to_listUsersFragment)
    }
}