package com.borlanddev.natife_finally.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.borlanddev.data.socket.Client
import com.borlanddev.data.storage.Prefs
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ChatVmFactory @AssistedInject constructor(
    @Assisted("recipientID") private val recipientID: String,
    private val client: Client,
    private val prefs: Prefs
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatVM(recipientID, client, prefs) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("recipientID") recipientID: String): ChatVmFactory
    }
}