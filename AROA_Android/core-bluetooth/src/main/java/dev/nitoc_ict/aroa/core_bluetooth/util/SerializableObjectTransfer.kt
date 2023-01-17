package dev.nitoc_ict.aroa.core_bluetooth.util

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.io.Serializable

class SerializableObjectTransfer<INPUT : Serializable, OUTPUT : Serializable>(
    private val inputStream: InputStream,
    private val outputStream: OutputStream
) {
    private val objectOutputStream = try {
        Log.d(SerializableObjectTransfer::class.simpleName, outputStream.toString())
        ObjectOutputStream(outputStream)
    } catch (e: Exception) {
        Log.d(SerializableObjectTransfer::class.simpleName, e.toString())
        e.printStackTrace()
        throw e
    }
    private val objectInputStream = try {
        Log.d(SerializableObjectTransfer::class.simpleName, inputStream.toString())
        ObjectInputStream(inputStream).apply {
            Log.d(SerializableObjectTransfer::class.simpleName, this.toString())
        }
    } catch (e: Exception) {
        Log.d(SerializableObjectTransfer::class.simpleName, e.toString())
        e.printStackTrace()
        throw e
    }

    private val coroutineScope = CoroutineScope(Job() + Dispatchers.IO)
    private var isActive = true
    private val sendQueue: MutableList<OUTPUT> = mutableListOf<OUTPUT>()

    fun getInputFlow(): Flow<INPUT> = flow {
        while (isActive) {
            val input = objectInputStream.readObject() as INPUT
            emit(input)
        }
    }.flowOn(Dispatchers.IO)

    fun sendSerializable(payload: OUTPUT) {
        sendQueue.add(payload)
        if (sendQueue.size <= 1) {
            startOutput()
        }

    }

    fun closeStream() {
        isActive = false
        objectInputStream.close()
        objectOutputStream.close()
    }

    private fun startOutput() {
        coroutineScope.launch(Dispatchers.IO) {
            while (sendQueue.isNotEmpty()) {
                objectOutputStream.writeObject(sendQueue.removeAt(0))
            }
        }
    }
}