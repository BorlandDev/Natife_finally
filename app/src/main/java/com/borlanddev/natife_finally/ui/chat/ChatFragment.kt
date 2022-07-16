package com.borlanddev.natife_finally.ui.chat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.borlanddev.natife_finally.R
import com.borlanddev.natife_finally.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatFragment : Fragment(R.layout.fragment_chat) {

    private var binding: FragmentChatBinding? = null
    private val chatVM: ChatVM by viewModels()
    private val args: ChatFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatBinding.bind(view)

        val recipientID = args.recipientID

        binding?.apply {
            sendMessageButton.setOnClickListener {
                val message = editText.text.toString()
                chatVM.sendMessage(message, recipientID)

                editText.text.clear()

            }
        }



        viewLifecycleOwner.lifecycleScope.launch {
            chatVM.message.collect {
                binding?.apply {
                    fromTextView.text = it.from.name + ":"
                    messageTextView.text = it.message

                }
            }
        }
    }
}

