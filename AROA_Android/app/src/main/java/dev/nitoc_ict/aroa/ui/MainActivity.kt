package dev.nitoc_ict.aroa.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.nitoc_ict.aroa.core_unity.RunningActivity
import dev.nitoc_ict.aroa.ui.theme.AROATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    private fun startRunningActivity() {
        val intent = Intent(this, RunningActivity::class.java)
        startActivity(intent)
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AROATheme {
    }
}