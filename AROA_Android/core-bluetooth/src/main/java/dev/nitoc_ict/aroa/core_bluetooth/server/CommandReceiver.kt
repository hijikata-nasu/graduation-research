package dev.nitoc_ict.aroa.core_bluetooth.server

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import dev.nitoc_ict.aroa.core_bluetooth.data.Command
import dev.nitoc_ict.aroa.core_bluetooth.data.Status
import dev.nitoc_ict.aroa.core_bluetooth.util.BluetoothInfo
import dev.nitoc_ict.aroa.core_bluetooth.util.SerializableObjectTransfer
import kotlinx.coroutines.*
import java.io.IOException

class CommandReceiver(val onReceiveCommand: (Command) -> Unit) {
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.IO)

    private var transfer: SerializableObjectTransfer<Command, Status>? = null
    private var serverSocket: BluetoothServerSocket? = null
    private var socket: BluetoothSocket? = null

    fun openConnection(bluetoothAdapter: BluetoothAdapter, onConnectionSuccess: () -> Unit = {}) {
        coroutineScope.launch(Dispatchers.IO) {
            Log.d(CommandReceiver::class.simpleName, "openingConnection")
            while (true) {
                serverSocket = try {
                    Log.d(CommandReceiver::class.simpleName, "serverSocketCreating...")
                    bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(
                        BluetoothInfo.BLUETOOTH_SERVICE_NAME,
                        BluetoothInfo.BLUETOOTH_UUID
                    )
                } catch (exception: SecurityException) {
                    Log.d(CommandReceiver::class.simpleName, "パーミッションエラー")
                    TODO("Bluetoothパーミッションが無い時のエラーハンドリング")
                }
                Log.d(CommandReceiver::class.simpleName, "サービスソケット確立完了")

                try {
                    Log.d(CommandReceiver::class.simpleName, "ソケット確立中")
                    socket = serverSocket!!.accept()
                    serverSocket?.close()
                    serverSocket = null
                    Log.d(CommandReceiver::class.simpleName, "ソケット確立!!")
                    transfer = SerializableObjectTransfer(socket!!.inputStream, socket!!.outputStream)
                    Log.d(CommandReceiver::class.simpleName, "Serializableインスタンス化")
                    onConnectionSuccess()
                    Log.d(CommandReceiver::class.simpleName, "接続完了")
                    transfer?.getInputFlow()?.collect(onReceiveCommand)
                    break
                } catch (exception: IOException) {
                    Log.d(CommandReceiver::class.simpleName, "再接続中……")
                    exception.printStackTrace()
                    /* 接続できない場合は再接続を試みる */
                    delay(3000)
                    continue
                }
            }
        }
    }

    fun sendStatus(status: Status) {
        transfer?.sendSerializable(status)
    }

    fun closeConnection() {
        coroutineScope.coroutineContext.cancelChildren()
        transfer?.closeStream()
        socket?.close()
        serverSocket?.close()
    }
}