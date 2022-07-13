package com.borlanddev.natife_finally.ui.authorization

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.borlanddev.natife_finally.R
import com.borlanddev.natife_finally.databinding.FragmentAuthorizationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthorizationFragment : Fragment(R.layout.fragment_authorization) {

    private var binding: FragmentAuthorizationBinding? = null
    private val authorizationVM: AuthorizationVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAuthorizationBinding.bind(view)

        if (authorizationVM.isSignedIn()) {
            authorizationVM.provideUsername()
            goToListUsers()
        }

        binding?.signUpButton?.setOnClickListener {
            val username = binding?.singInTextInput?.text.toString()
            if (username.isEmpty()) {
                val toast = Toast.makeText(
                    requireContext(),
                    R.string.wrong_authorization,
                    Toast.LENGTH_LONG
                )
                toast.show()
            } else {
                    authorizationVM.authorization(username)
                }

                binding?.also {
                    it.progressBar.visibility = View.VISIBLE
                    it.signUpButton.isEnabled = false
                    it.singInTextInput.isEnabled = false
                }
                goToListUsers()
            }
        }


    private fun goToListUsers() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            authorizationVM.isSigned.collect {
                if (it) {
                    binding?.also { view ->
                        view.progressBar.visibility = View.INVISIBLE
                        view.signUpButton.isEnabled = true
                        view.singInTextInput.isEnabled = true
                    }
                    findNavController().navigate(R.id.action_authorizationFragment_to_listUsersFragment)
                }
            }
        }
    }
}


