package com.borlanddev.natife_finally.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.borlanddev.natife_finally.databinding.ViewChatBinding
import com.borlanddev.natife_finally.helpers.DEFAULT_NAME_PREFS
import com.borlanddev.natife_finally.model.MessageDto

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ChatHolder>() {

    private val listMessage = mutableListOf<Any>()
    private var currentUsername: String = DEFAULT_NAME_PREFS

    class ChatHolder(private val binding: ViewChatBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindToUser(sendMessage: String, currentUsername: String) {
            binding.apply {
                userMessageLayout.visibility = View.VISIBLE
                recipientMessageLayout.visibility = View.INVISIBLE

                userFrom.text = currentUsername
                chatUserMessage.text = sendMessage
            }
        }

        fun bindToRecipient(messageDto: MessageDto) {
            binding.apply {
                recipientMessageLayout.visibility = View.VISIBLE
                userMessageLayout.visibility = View.INVISIBLE

                recipientFrom.text = messageDto.from.name
                chatRecipientMessage.text = messageDto.message
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewChatBinding.inflate(inflater, parent, false)
        return ChatHolder(binding)
    }

    override fun getItemCount(): Int = listMessage.size

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        when (val item = listMessage[position]) {
            is String -> holder.bindToUser(item, currentUsername)
            is MessageDto -> holder.bindToRecipient(item)
        }
    }

    fun sentMessage(sendMessage: String, username: String) {
        currentUsername = username
        listMessage.add(sendMessage)
        notifyDataSetChanged()
    }

    fun newMessage(messageDto: MessageDto) {
        listMessage.add(messageDto)
        notifyDataSetChanged()
    }
}

