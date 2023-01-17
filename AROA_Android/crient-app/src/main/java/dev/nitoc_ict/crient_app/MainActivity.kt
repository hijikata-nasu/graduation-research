package dev.nitoc_ict.crient_app

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.nitoc_ict.aroa.core_bluetooth.data.Command
import dev.nitoc_ict.crient_app.ui.theme.AROATheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bluetoothManager = this.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        val deviceList = try {
            bluetoothAdapter.bondedDevices.toList()
        } catch (exception: SecurityException) {
            exception.printStackTrace()
            throw exception
        }
        setContent {
            AROATheme {
                Scaffold {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        val bluetoothConnectState = viewModel.bluetoothConnected.collectAsState()
                        val time = viewModel.timeStateFlow.collectAsState()
                        val speed = viewModel.speedStateFlow.collectAsState()
                        val distance = viewModel.distanceStateFlow.collectAsState()
                        val rank = viewModel.rankStateFlow.collectAsState()
                        var speedSetting by remember {
                            mutableStateOf(1.0f)
                        }
                        var distanceSetting by remember {
                            mutableStateOf(1.0f)
                        }

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(text = "time:${time.value}")
                            Spacer(modifier = Modifier.weight(1.0f))
                            Button(onClick = {
                                viewModel.sendCommand(Command.ResetTimer)
                            }) {
                                Text(text = "Reset")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = {
                                viewModel.sendCommand(Command.ResumeTimer)
                            }) {
                                Text(text = "Resume")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = {
                                viewModel.sendCommand(Command.StopTimer)
                            }) {
                                Text(text = "Stop")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(text = "speed:${speed.value}")
                            Spacer(modifier = Modifier.weight(1.0f))
                            Button(onClick = {
                                viewModel.sendCommand(Command.SetSpeed(0))
                            }) {
                                Text(text = "ResetSpeed")
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Slider(
                                value = speedSetting,
                                valueRange = -5f..5f,
                                onValueChange = {
                                    speedSetting = it
                                    viewModel.sendCommand(Command.SetSpeed(it.toInt()))
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(text = "rank:${rank.value}")
                            Spacer(modifier = Modifier.weight(1.0f))
                            Button(onClick = {
                                viewModel.sendCommand(Command.SetRank(10))
                            }) {
                                Text(text = "RESET")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = {
                                viewModel.sendCommand(Command.SetRank(rank.value - 1))
                            }) {
                                Text(text = "DOWN")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = {
                                viewModel.sendCommand(Command.SetRank(rank.value + 1))
                            }) {
                                Text(text = "UP")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(text = "distance:${distance.value}")
                            Spacer(modifier = Modifier.weight(1.0f))
                            Button(onClick = {
                                viewModel.sendCommand(Command.SetDistance(0))
                            }) {
                                Text(text = "Reset")
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Slider(
                                value = distanceSetting,
                                valueRange = 0f..40f,
                                onValueChange = {
                                    distanceSetting = it
                                    viewModel.sendCommand(Command.SetDistance(it.toInt()))
                                }
                            )
                        }

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Spacer(modifier = Modifier.weight(1.0f))
                            Button(
                                onClick = { viewModel.sendCommand(Command.ShowResult) },
                            ) {
                                Text(text = "Show Result")
                            }
                        }

                        Spacer(modifier = Modifier.weight(1.0f))
                        if (bluetoothConnectState.value.not()) {
                            @SuppressLint("MissingPermission")
                            for (devise in deviceList) {
                                Button(onClick = {
                                    viewModel.openConnection(devise)
                                },
                                ) {
                                    Text(text = devise.name)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        viewModel.closeConnection()
        super.onDestroy()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AROATheme {
        Scaffold {
            Column(
                Modifier.padding(16.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    val time = 100
                    Text(text = "time:${time}")
                    Spacer(modifier = Modifier.weight(1.0f))
                    Button(onClick = { }) {
                        Text(text = "Reset")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    val speed = 100
                    Text(text = "speed:${speed}")
                    Spacer(modifier = Modifier.weight(1.0f))
                    Button(onClick = { }) {
                        Text(text = "Reset")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    val rank = 100
                    Text(text = "rank:${rank}")
                    Spacer(modifier = Modifier.weight(1.0f))
                    Button(onClick = { }) {
                        Text(text = "Reset")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    val distance = 100
                    Text(text = "distance:${distance}")
                    Spacer(modifier = Modifier.weight(1.0f))
                    Button(onClick = { }) {
                        Text(text = "Reset")
                    }
                }
            }
        }
    }
}