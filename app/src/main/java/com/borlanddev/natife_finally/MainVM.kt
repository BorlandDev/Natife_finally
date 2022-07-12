package com.borlanddev.natife_finally


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borlanddev.natife_finally.socket.Client
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(

) : ViewModel() {

    private val client = Client()

    private val _data = MutableLiveData(listOf<Int>())
    val data = _data

    init {
        getConnectClientA()
    }


    private fun getConnectClientA() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                client.getToConnection()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


}