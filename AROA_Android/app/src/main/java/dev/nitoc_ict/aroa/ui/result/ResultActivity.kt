package dev.nitoc_ict.aroa.ui.result

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.nitoc_ict.aroa.core_bluetooth.server.BluetoothService
import dev.nitoc_ict.aroa.ui.MainActivity
import dev.nitoc_ict.aroa.ui.theme.AROATheme

class ResultActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rank = intent.getIntExtra(BluetoothService.INTENT_EXTRA_RANK, -1)
        val time = intent.getIntExtra(BluetoothService.INTENT_EXTRA_TIME, -1)

        val timeHour = time / 3600
        val timeMin = (time % 3600) / 60
        val timeSec = time % 60

        setContent {
            AROATheme {
                Scaffold {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            text = "Time:${timeHour}時間${timeMin}分${timeSec}秒",
                            fontSize = 16.sp
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            text = "Rank:${rank}位",
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.weight(1.0f))
                        Button(onClick = {
                            intentMainActivity()
                        }) {
                            Text(text = "BachToHome")
                        }
                    }
                }
            }
        }
    }

    private fun intentMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AROATheme {
        Scaffold {
            Column(
                Modifier
                    .padding(16.dp)
            ) {
                Text(text = "Time:0時間12分34秒")
                Text(text = "Rank:53位")
            }
        }
    }
}