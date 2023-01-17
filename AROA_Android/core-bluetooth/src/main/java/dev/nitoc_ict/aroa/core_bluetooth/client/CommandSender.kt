package dev.nitoc_ict.aroa.core_bluetooth.client

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import dev.nitoc_ict.aroa.core_bluetooth.data.Command
import dev.nitoc_ict.aroa.core_bluetooth.data.Status
import dev.nitoc_ict.aroa.core_bluetooth.util.BluetoothInfo
import dev.nitoc_ict.aroa.core_bluetooth.util.SerializableObjectTransfer
import kotlinx.coroutines.*
import java.io.IOException

class CommandSender(val onReceiveStatus: (Status) -> Unit) {
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.IO)

    private var transfer: SerializableObjectTransfer<Status, Command>? = null
    private var serverSocket: BluetoothServerSocket? = null
    private var socket: BluetoothSocket? = null

    fun openConnection(bluetoothDevice: BluetoothDevice, onConnectionSuccess: () -> Unit = {}) {
        coroutineScope.launch(Dispatchers.IO) {
            while (true) {
                Log.d(CommandSender::class.simpleName, "socket_init")
                socket = try {
                    bluetoothDevice.createRfcommSocketToServiceRecord(
                        BluetoothInfo.BLUETOOTH_UUID
                    ).apply {
                        connect()
                    }
                } catch (exception: SecurityException) {
                    TODO("Bluetoothパーミッションが無い時のエラーハンドリング")
                } catch (ioException: IOException) {
                    Log.d(CommandSender::class.simpleName, ioException.message.toString())
                    throw ioException
                }

                try {
                    Log.d(CommandSender::class.simpleName, "Transfer初期化")
                    transfer = SerializableObjectTransfer(socket!!.inputStream, socket!!.outputStream)
                    Log.d(CommandSender::class.simpleName, "接続完了")
                    onConnectionSuccess()
                    coroutineScope.launch {
                        transfer?.getInputFlow()?.collect(onReceiveStatus)
                    }
                    break
                } catch (exception: IOException) {
                    exception.printStackTrace()
                    Log.d(CommandSender::class.simpleName, "接続失敗。再試行します")
                    /* 接続できない場合は再接続を試みる */
                    delay(3000)
                    continue
                } catch (e: java.lang.Exception) {
                    Log.d(CommandSender::class.simpleName, "接続失敗。再試行します")
                    e.printStackTrace()
                }
                Log.d(CommandSender::class.simpleName, "ループ終了")
            }
        }
    }

    fun sendCommand(command: Command) {
        transfer?.sendSerializable(command)
    }

    fun closeConnection() {
        coroutineScope.coroutineContext.cancelChildren()
        transfer?.closeStream()
        socket?.close()
        serverSocket?.close()
    }
}