package com.borlanddev.natife_finally


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borlanddev.natife_finally.socket.ClientA
import com.borlanddev.natife_finally.socket.ClientB
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(

) : ViewModel() {

    private val clientA = ClientA()
    private val clientB = ClientB()

    private val _data = MutableLiveData(listOf<Int>())
    val data = _data

    init {
        getConnectClientA()
        getConnectClientB()
    }


    private fun getConnectClientA() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                clientA.getToConnection()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getConnectClientB() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                clientB.getToConnection()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}