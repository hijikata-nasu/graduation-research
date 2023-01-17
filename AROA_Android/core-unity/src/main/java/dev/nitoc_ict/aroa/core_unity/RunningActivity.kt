package dev.nitoc_ict.aroa.core_unity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.unity3d.player.UnityPlayerActivity
import dev.nitoc_ict.aroa.core_bluetooth.server.BluetoothService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RunningActivity: UnityPlayerActivity() {

    private var _bluetoothService: BluetoothService? = null
    private val bluetoothService: BluetoothService get() = checkNotNull(_bluetoothService)
    private var isBound: Boolean = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
            val binder = service as BluetoothService.BluetoothBinder
            isBound = true
            _bluetoothService = binder.getService()
            Log.d(RunningActivity::class.java.simpleName, "ServiceConnected!")
            coroutineScope.launch(Dispatchers.IO) {
                delay(10000)
                unityPlayer.setRank(100)
                delay(1000)
                bluetoothService.startCount()
            }
            coroutineScope.launch {
                bluetoothService.timeStateFlow.collect {time ->
                    unityPlayer.setTime(time)
                }
            }

            coroutineScope.launch {
                bluetoothService.rankStateFlow.collect {rank ->
                    unityPlayer.setRank(rank)
                }
            }

            coroutineScope.launch {
                bluetoothService.distanceStateFlow.collect {distance ->
                    unityPlayer.setDistance(distance)
                }
            }
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            Log.d(RunningActivity::class.simpleName, "ServiceDisconnected")
            isBound = false
        }
    }

    override fun onStart() {
        super.onStart()

        val intent = Intent(this, BluetoothService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)

    }

    override fun onStop() {
        super.onStop()
        Log.d(RunningActivity::class.simpleName, "onStop")
        unbindService(connection)
        isBound = false
    }
}