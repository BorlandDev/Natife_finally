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

        // Если мы авторизованы
        if (authorizationVM.isSignedIn()) {
            authorizationVM.authorization()
            // Нужно дождатся окончания авторизации и тогда перейти на экран
            isSignedIn()
        }

        // Если не авторизованы - введите не пустое Имя
        binding?.signUpButton?.setOnClickListener {
            val username = binding?.singInTextInput?.text.toString()
            if (username.isEmpty()) {
                val toast = Toast.makeText(
                    requireContext(),
                    R.string.wrong_authorization,
                    Toast.LENGTH_SHORT
                )
                toast.show()
            } else {
                // Запускаем авторизацию
                authorizationVM.authorization(username)
                // Нужно дождатся окончания авторизации и тогда перейти на экран
                isSignedIn()
            }
        }
    }

    private fun isSignedIn() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            authorizationVM.singedInVM.collect {
                if (!it) {
                    binding?.apply {
                        progressBar.visibility = View.VISIBLE
                        signUpButton.isEnabled = false
                        singInTextInput.isEnabled = false
                    }
                } else {
                    goToListUsers()
                }
            }
        }
    }

    private fun goToListUsers() {
        binding?.apply {
            progressBar.visibility = View.INVISIBLE
            signUpButton.isEnabled = true
            singInTextInput.isEnabled = true

            findNavController().navigate(R.id.action_authorizationFragment_to_listUsersFragment)
        }
    }


}



