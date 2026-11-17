package com.simpletube.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.simpletube.app.data.NewPipeClient
import com.simpletube.app.ui.SimpleTubeApp
import com.simpletube.app.ui.theme.SimpleTubeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NewPipeClient.init()
        enableEdgeToEdge()
        setContent {
            SimpleTubeTheme {
                SimpleTubeApp()
            }
        }
    }
}
