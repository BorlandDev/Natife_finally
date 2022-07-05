package com.borlanddev.natife_finally.ui.chat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.borlanddev.natife_finally.R
import com.borlanddev.natife_finally.databinding.FragmentChatBinding

class ChatFragment() : Fragment(R.layout.fragment_chat) {

    private var binding: FragmentChatBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatBinding.bind(view)

    }
}