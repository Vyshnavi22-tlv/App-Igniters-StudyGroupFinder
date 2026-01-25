package com.example.studee

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.composable
@Composable
fun DashboardScreen(onCreateNew: () -> Unit) {

    // Sample study groups - later you can connect to Firebase
    val groups = remember {
        mutableStateListOf(
            "Math Warriors",
            "AI Researchers",
            "Physics Legends",
            "Competitive Programming Squad"
        )
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "Study Groups",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(16.dp))

            // Group List
            groups.forEach { group ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(text = group, style = MaterialTheme.typography.titleMedium)
                        Text(text = "Tap to view details", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Create New Group Button
            Button(
                onClick = onCreateNew,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("➕ Create New Study Group")
            }
        }
    }
}

@Composable
fun DashboardScreen(
    viewModel: StudyGroupViewModel,
    onCreateNew: () -> Unit
) {
    val groups = viewModel.groups  // State-aware list

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text("Study Groups", style = MaterialTheme.typography.headlineMedium)

            Spacer(Modifier.height(16.dp))

            groups.forEach { group ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(group, style = MaterialTheme.typography.titleMedium)
                        Text("Tap to view details", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = onCreateNew,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("➕ Create New Study Group")
            }
        }
    }
}