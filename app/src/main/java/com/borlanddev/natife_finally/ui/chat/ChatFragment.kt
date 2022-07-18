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
import com.borlanddev.natife_finally.model.MessageDto
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : Fragment(R.layout.fragment_chat) {

    private var binding: FragmentChatBinding? = null
    private val args: ChatFragmentArgs by navArgs()

    @Inject
    lateinit var factory: ChatVmFactory.Factory

    private val chatVM: ChatVM by viewModels {
        factory.create(args.recipientID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatBinding.bind(view)

        val chatAdapter = ChatAdapter(chatVM.clientId)

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
                    chatVM.sendMessage(message)
                    editText.text.clear()
                }
            }

           lifecycleScope.launch {
                chatVM.newMessage.collect {
                    val messageList = mutableListOf<MessageDto>()
                    messageList.add(it)
                    chatAdapter.submitList(messageList)
                }
            }
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = chatAdapter
        }
    }
}

