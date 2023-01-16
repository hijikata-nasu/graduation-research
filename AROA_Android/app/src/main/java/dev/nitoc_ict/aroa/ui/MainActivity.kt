package dev.nitoc_ict.aroa.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.unity3d.player.UnityPlayerActivity
import dev.nitoc_ict.aroa.ui.theme.AROATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AROATheme {
                Scaffold(
                    floatingActionButton = {
                        ExtendedFloatingActionButton(
                            onClick = { startUnityPlayerActivity() },
                            text = { Text(text = "Start") }
                        )
                    }
                ) {
                }
            }
        }
    }

    private fun startUnityPlayerActivity() {
        val intent = Intent(this, UnityPlayerActivity::class.java)
        startActivity(intent)
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AROATheme {
    }
}