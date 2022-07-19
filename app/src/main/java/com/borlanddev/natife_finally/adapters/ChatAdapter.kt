package com.borlanddev.natife_finally.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.borlanddev.natife_finally.databinding.ClientMessageLayoutBinding
import com.borlanddev.natife_finally.databinding.RecipientMesageLayoutBinding
import com.borlanddev.data.model.MessageDto

class ChatAdapter(private val checkID: (MessageDto) -> Boolean) :
    ListAdapter<MessageDto, RecyclerView.ViewHolder>(MessageDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        val messageDto = getItem(position)
        return if (checkID.invoke(messageDto)) {
            CLIENT_VIEW_TYPE
        } else {
            RECIPIENT_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CLIENT_VIEW_TYPE -> createClientHolder(parent)
            RECIPIENT_VIEW_TYPE -> createRecipientHolder(parent)
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            CLIENT_VIEW_TYPE ->
                (holder as? ClientHolder)?.bind(getItem(position))
            RECIPIENT_VIEW_TYPE ->
                (holder as? RecipientHolder)?.bind(getItem(position))
        }
    }

    class ClientHolder(private val binding: ClientMessageLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(messageDto: MessageDto) {
            binding.apply {
                clientFrom.text = messageDto.from.name
                clientMessage.text = messageDto.message
            }
        }
    }

    class RecipientHolder(private val binding: RecipientMesageLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(messageDto: MessageDto) {
            binding.apply {
                recipientFrom.text = messageDto.from.name
                recipientMessage.text = messageDto.message
            }
        }
    }

    companion object {

        const val CLIENT_VIEW_TYPE = 0
        const val RECIPIENT_VIEW_TYPE = 1

        fun createClientHolder(parent: ViewGroup): ClientHolder {
            return ClientHolder(
                ClientMessageLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        fun createRecipientHolder(parent: ViewGroup): RecipientHolder {
            return RecipientHolder(
                RecipientMesageLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    class MessageDiffCallback
        : DiffUtil.ItemCallback<MessageDto>() {

        override fun areItemsTheSame(oldItem: MessageDto, newItem: MessageDto): Boolean {
            return oldItem.from.id == newItem.from.id
        }

        override fun areContentsTheSame(oldItem: MessageDto, newItem: MessageDto): Boolean {
            return oldItem == newItem
        }
    }
}