package com.example.carddeck.ui.group

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.icons.Icons
import androidx.compose.material3.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.max

@Composable
fun GroupViewScreen(
    sessionCode: String,
    state: GroupViewUiState,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Group View") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Session Code: $sessionCode", fontWeight = FontWeight.SemiBold)
            when {
                state.isLoading -> Text(text = "Loading responses...")
                state.errorMessage != null -> Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
                state.aggregates.isEmpty() -> Text("No responses yet.")
                else -> EmotionAggregatesList(state)
            }
        }
    }
}

@Composable
private fun EmotionAggregatesList(state: GroupViewUiState) {
    val maxCount = max(state.aggregates.maxOfOrNull { it.count } ?: 1, 1)
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(state.aggregates) { aggregate ->
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "${aggregate.emotionCard.name} (${aggregate.count})", fontWeight = FontWeight.SemiBold)
                LinearProgressIndicator(
                    progress = aggregate.count / maxCount.toFloat(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }
        }
    }
}
