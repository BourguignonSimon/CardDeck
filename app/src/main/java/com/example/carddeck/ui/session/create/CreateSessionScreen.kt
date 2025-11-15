package com.example.carddeck.ui.session.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.icons.Icons
import androidx.compose.material3.icons.filled.Refresh
import androidx.compose.material3.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CreateSessionScreen(
    state: CreateSessionUiState,
    onCreateAnother: () -> Unit,
    onOpenGroupView: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Session") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onCreateAnother, enabled = !state.isLoading) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "New Session")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Share this code with participants", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = state.sessionCode ?: "------",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold
            )
            if (state.errorMessage != null) {
                Text(text = state.errorMessage, color = MaterialTheme.colorScheme.error)
            }
            Button(
                onClick = { state.sessionCode?.let(onOpenGroupView) },
                enabled = state.sessionCode != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Open Group View")
            }
        }
    }
}
