package dev.nitoc_ict.aroa.core_bluetooth.server

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import dev.nitoc_ict.aroa.core_bluetooth.data.Command
import dev.nitoc_ict.aroa.core_bluetooth.data.Status
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BluetoothService : Service() {
    companion object {
        private const val SERVICE_ID = 1
        private const val NOTIFICATION_CHANNEL_ID = "BLUE_TOOTH_SERVICE"
        private const val NOTIFICATION_CHANNEL_NAME = "Bluetoothサービス"
        private const val NOTIFICATION_CHANNEL_DESCRIPTION = "Bluetooth通信をする為のサービスの状態を表示する通知です"
        private const val ACTION_SERVICE_PING = "dev.nitoc_ict.aroa.BluetoothService_is_running"

        private var resultActivityClass: Class<*>? = null

        const val INTENT_EXTRA_RANK = "RANK"
        const val INTENT_EXTRA_TIME = "TIME"

        fun setResultActivity(activityClass: Class<*>) {
            resultActivityClass = activityClass
        }

        fun isActive(context: Context): Boolean =
            LocalBroadcastManager
                .getInstance(context)
                .sendBroadcast(Intent(ACTION_SERVICE_PING))
    }

    private val binder = BluetoothBinder()

    private val coroutineScope = CoroutineScope(Job() + Dispatchers.IO)
    private var counterJob: Job? = null

    private val broadcastManager: LocalBroadcastManager by lazy {
        LocalBroadcastManager.getInstance(this)
    }
    private val broadcastReceiverPing = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            /* Nothing To Do */
        }
    }

    private val commandReceiver = CommandReceiver() { command ->
        when (command) {
            Command.StopTimer -> {
                stopCount()
            }
            Command.ResetTimer -> {
                resetCount()
            }
            Command.ResumeTimer -> {
                startCount()
            }
            is Command.SetDistance -> {
                setDistance(command.meter)
            }
            is Command.SetRank -> {
                setRank(command.rank)
            }
            is Command.SetSpeed -> {
                setSpeed(command.meterParSec)
            }
            Command.ShowResult -> {
                intentResultActivity()
            }
        }
    }

    private val _timeFlow = MutableStateFlow(0)
    private val _rankFlow = MutableStateFlow(10)
    private val _distanceFlow = MutableStateFlow(40)
    private var relativeVelocity = 0

    val timeStateFlow: StateFlow<Int> = _timeFlow
    val rankStateFlow: StateFlow<Int> = _rankFlow
    val distanceStateFlow: StateFlow<Int> = _distanceFlow

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val bluetoothManager : BluetoothManager =
            applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter

        Log.d(BluetoothService::class.simpleName, "onStartCommand")
        commandReceiver.openConnection(bluetoothAdapter) {
            coroutineScope.launch {
                while (true) {
                    commandReceiver.sendStatus(
                        Status(
                            timeStateFlow.value,
                            relativeVelocity,
                            rankStateFlow.value,
                            distanceStateFlow.value
                        )
                    )
                    delay(15)
                }
            }
        }
        val notification = createNotification()

        startForeground(SERVICE_ID, notification)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        broadcastManager.registerReceiver(broadcastReceiverPing, IntentFilter(ACTION_SERVICE_PING))
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        commandReceiver.closeConnection()
        broadcastManager.unregisterReceiver(broadcastReceiverPing)
    }

    fun startCount() {
        counterJob = coroutineScope.launch {
            while (true) {
                delay(1000)
                _timeFlow.emit(_timeFlow.value + 1)
                checkRank()
            }
        }
    }

    private fun checkRank(){
        if(rankStateFlow.value == 1) {
            return
        }

        if ( relativeVelocity > 0 ) {
            _distanceFlow.value -= relativeVelocity
            if ( _distanceFlow.value < 0 ) {
                _distanceFlow.value = 40
                _rankFlow.value -= 1
            }
        } else if ( relativeVelocity < 0 ) {
            _distanceFlow.value -= relativeVelocity
            if ( _distanceFlow.value > 40 ) {
                _distanceFlow.value = 0
                _rankFlow.value += 1
            }
        }
    }

    private fun stopCount() {
        counterJob?.cancel()
    }

    private fun resetCount() {
        counterJob?.cancel()
        _timeFlow.value = 0
    }

    private fun setDistance(meter: Int) {
        _distanceFlow.value = meter
    }

    private fun setSpeed(meterParSec: Int) {
        relativeVelocity = meterParSec
    }

    private fun setRank(rank: Int) {
        _rankFlow.value = rank
    }

    private fun intentResultActivity() {
        if (resultActivityClass == null) {
            throw java.lang.IllegalStateException("resultActivityが設定されていません。")
        }
        val intent = Intent(applicationContext, resultActivityClass).apply {
            putExtra(INTENT_EXTRA_RANK, rankStateFlow.value)
            putExtra(INTENT_EXTRA_TIME, timeStateFlow.value)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    private fun createNotification(): Notification {
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

        return Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Bluetoothサービス")
            .build()
    }

    inner class BluetoothBinder : Binder() {
        fun getService(): BluetoothService = this@BluetoothService
    }
}