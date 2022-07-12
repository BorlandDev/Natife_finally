package com.borlanddev.natife_finally.ui.authorization

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.borlanddev.natife_finally.R
import com.borlanddev.natife_finally.databinding.FragmentAuthorizationBinding
import com.borlanddev.natife_finally.helpers.APP_PREFERENCES
import com.borlanddev.natife_finally.helpers.Prefs
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthorizationFragment : Fragment(R.layout.fragment_authorization) {

    private var binding: FragmentAuthorizationBinding? = null

    @Inject
    lateinit var prefs: Prefs

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAuthorizationBinding.bind(view)

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
                prefs.preferences.edit().putString(APP_PREFERENCES, username).apply()

                findNavController().navigate(R.id.action_authorizationFragment_to_listUsersFragment)
            }
        }
    }
}

