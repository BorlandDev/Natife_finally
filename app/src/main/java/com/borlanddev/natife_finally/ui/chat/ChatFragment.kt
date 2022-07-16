package com.borlanddev.natife_finally.ui.chat

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.borlanddev.natife_finally.R
import com.borlanddev.natife_finally.adapters.ChatAdapter
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
        val chatAdapter = ChatAdapter()

        binding?.apply {
            sendMessageButton.setOnClickListener {
                val message = editText.text.toString()
                if (message.isEmpty()) {
                    val toast = Toast.makeText(
                        requireContext(),
                        R.string.wrong_sending_message,
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else {
                    chatVM.sendMessage(message, recipientID)
                    editText.text.clear()
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                chatVM.sendMessage.collect {
                    chatAdapter.sendMessage(it, chatVM.getUsername())
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                chatVM.newMessage.collect {
                    chatAdapter.newMessage(it)
                }
            }



            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = chatAdapter
        }
    }
}

