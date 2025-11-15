package com.example.carddeck.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.carddeck.ui.navigation.CardDeckNavHost

@Composable
fun CardDeckApp() {
    val navController = rememberNavController()
    Surface(color = MaterialTheme.colorScheme.background) {
        CardDeckNavHost(navController = navController, modifier = Modifier)
    }
}
