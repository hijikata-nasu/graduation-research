package dev.nitoc_ict.aroa.core_bluetooth

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class BluetoothService : Service() {
    companion object {
        val BLUETOOTH_UUID = UUID.fromString(
            "e050f49a-e63f-8e7d-dd5b-b439e4085520"
        )
        const val NOTIFICATION_CHANNEL_ID = "BLUE_TOOTH_SERVICE"
        const val SERVICE_ID = 1
        const val NOTIFICATION_CHANNEL_NAME = "Bluetoothサービス"
        const val NOTIFICATION_CHANNEL_DESCRIPTION = "Bluetooth通信をする為のサービスの状態を表示する通知です"
    }


    private val binder = BluetoothBinder()

    private val scope = CoroutineScope(Job() + Dispatchers.IO)
    private var counterJob: Job? = null

    private val _timeFlow = MutableStateFlow(0)
    private val _rankFlow = MutableStateFlow(0)
    private val _distanceFlow = MutableStateFlow(0)

    val timeStateFlow: StateFlow<Int> = _timeFlow
    val rankStateFlow: StateFlow<Int> = _rankFlow
    val distanceStateFlow: StateFlow<Int> = _distanceFlow

    fun startCount() {
        counterJob?.cancel()

        counterJob = scope.launch {
            while (true) {
                delay(1000)
                _timeFlow.emit(_timeFlow.value + 1)
                _rankFlow.emit(_timeFlow.value / 5)
                _distanceFlow.emit(_timeFlow.value % 5)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (manager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = NOTIFICATION_CHANNEL_DESCRIPTION
            }

            manager.createNotificationChannel(channel)
        }

        val notification: Notification = Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Bluetoothサービス")
            .build()

        startForeground(SERVICE_ID, notification)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }

    inner class BluetoothBinder : Binder() {
        fun getService(): BluetoothService = this@BluetoothService
    }
}