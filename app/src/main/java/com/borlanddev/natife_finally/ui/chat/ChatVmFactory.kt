package com.borlanddev.natife_finally.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.borlanddev.natife_finally.socket.Client
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ChatVmFactory @AssistedInject constructor(
    @Assisted("recipientID") private val recipientID: String,
    private val client: Client
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatVM(recipientID, client) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("recipientID") recipientID: String): ChatVmFactory
    }
}