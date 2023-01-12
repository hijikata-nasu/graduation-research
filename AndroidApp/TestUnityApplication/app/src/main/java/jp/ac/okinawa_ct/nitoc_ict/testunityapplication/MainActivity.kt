package jp.ac.okinawa_ct.nitoc_ict.testunityapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.unity3d.player.UnityPlayerActivity
import jp.ac.okinawa_ct.nitoc_ict.testunityapplication.ui.theme.TestUnityApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestUnityApplicationTheme {
                Scaffold(
                    floatingActionButton = {
                        ExtendedFloatingActionButton(
                            onClick = { startUnityPlayerActivity() },
                            text = { Text(text = "Unity") }
                        )
                    }
                ) {
                }
            }
        }
    }

    private fun startUnityPlayerActivity(){
        val intent = Intent(this, UnityPlayerActivity::class.java)
        startActivity(intent)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /*TODO*/ },
                text = { Text(text = "Unity") }
            )
        }
    ) {
    }
}