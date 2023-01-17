package dev.nitoc_ict.crient_app

import android.app.Application
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.nitoc_ict.aroa.core_bluetooth.client.CommandSender
import dev.nitoc_ict.aroa.core_bluetooth.data.Command
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application){
    private val commandSender = CommandSender { status ->
        _timeFlow.value = status.time
        _speedFlow.value = status.speed
        _rankFlow.value = status.rank
        _distanceFlow.value = status.distance
    }

    private val _timeFlow = MutableStateFlow(-1)
    private val _speedFlow = MutableStateFlow(-1)
    private val _rankFlow = MutableStateFlow(-1)
    private val _distanceFlow = MutableStateFlow(-1)
    private val _bluetoothConnected = MutableStateFlow(false)

    val timeStateFlow: StateFlow<Int> = _timeFlow
    val speedStateFlow: StateFlow<Int> = _speedFlow
    val rankStateFlow: StateFlow<Int> = _rankFlow
    val distanceStateFlow: StateFlow<Int> = _distanceFlow
    val bluetoothConnected: StateFlow<Boolean> = _bluetoothConnected

    fun openConnection(bluetoothDevice: BluetoothDevice) {
        Log.d(MainViewModel::class.simpleName, "Connecting...")
        commandSender.openConnection(bluetoothDevice) {
            Log.d(MainViewModel::class.simpleName, "ConnectionSuccess!!")
            _bluetoothConnected.value = true
        }
    }

    fun sendCommand(command: Command) {
        commandSender.sendCommand(command)
    }

    fun closeConnection() {
        commandSender.closeConnection()
    }
}