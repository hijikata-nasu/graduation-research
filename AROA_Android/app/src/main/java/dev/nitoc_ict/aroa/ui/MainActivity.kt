package dev.nitoc_ict.aroa.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.nitoc_ict.aroa.core_bluetooth.server.BluetoothService
import dev.nitoc_ict.aroa.core_unity.RunningActivity
import dev.nitoc_ict.aroa.ui.result.ResultActivity
import dev.nitoc_ict.aroa.ui.theme.AROATheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpBluetoothService()

        setContent {
            AROATheme {
                Scaffold(
                    floatingActionButton = {
                        ExtendedFloatingActionButton(
                            onClick = { startRunningActivity() },
                            text = { Text(text = "Start") }
                        )
                    }
                ) {
                }
            }
        }
    }

    private fun setUpBluetoothService() {
        Log.d(MainActivity::class.simpleName, "setUpBluetoothService")
        if (BluetoothService.isActive(this).not()) {
            Log.d(MainActivity::class.simpleName, "startBluetoothService")
            BluetoothService.setResultActivity(ResultActivity::class.java)
            startBluetoothService()
        }
    }

    private fun startBluetoothService() {
        val intent = Intent(this, BluetoothService::class.java)
        startForegroundService(intent)
    }

    private fun startRunningActivity() {
        val intent = Intent(this, RunningActivity::class.java)
        startActivity(intent)
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AROATheme {
        Scaffold(
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = { },
                    text = { Text(text = "Start") }
                )
            }
        ) {
        }
    }
}