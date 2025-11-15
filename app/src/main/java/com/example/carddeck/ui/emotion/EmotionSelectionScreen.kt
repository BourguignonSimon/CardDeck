package com.example.carddeck.ui.emotion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.carddeck.ui.components.EmotionCardItem

@Composable
fun EmotionSelectionScreen(
    state: EmotionSelectionUiState,
    onEmotionClick: (String) -> Unit,
    onSubmit: () -> Unit,
    onSubmissionHandled: () -> Unit,
    onFinished: () -> Unit
) {
    if (state.submissionCompleted) {
        LaunchedEffect(state.submissionCompleted) {
            onSubmissionHandled()
            onFinished()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Select Emotions") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Choose up to three emotions that best describe how you feel right now.",
                style = MaterialTheme.typography.bodyMedium
            )
            LazyVerticalGrid(
                columns = GridCells.Adaptive(160.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f, fill = true)
            ) {
                items(state.emotions, key = { it.id }) { card ->
                    EmotionCardItem(
                        card = card,
                        isSelected = state.selectedEmotionIds.contains(card.id),
                        onClick = { onEmotionClick(card.id) }
                    )
                }
            }
            if (state.errorMessage != null) {
                Text(text = state.errorMessage, color = MaterialTheme.colorScheme.error)
            }
            Button(
                onClick = onSubmit,
                enabled = state.selectedEmotionIds.isNotEmpty() && state.selectedEmotionIds.size <= 3 && !state.isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(text = if (state.isSubmitting) "Submitting..." else "Submit")
            }
        }
    }
}
