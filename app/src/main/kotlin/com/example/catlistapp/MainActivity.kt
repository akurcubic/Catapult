package com.example.catlistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.catlistapp.core.theme.CatListAppTheme
import com.example.catlistapp.navigation.CatNavigation
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CatListAppTheme {
                CatNavigation()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CatListAppTheme {
        CatNavigation()
    }
}