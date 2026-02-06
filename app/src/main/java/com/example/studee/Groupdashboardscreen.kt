package com.example.studee

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDashboardScreen(
    groupId: String,
    viewModel: StudyGroupViewModel,
    onBack: () -> Unit
) {
    val group = viewModel.getGroupById(groupId)
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Overview", "Notes", "Members")
    var showLeaveDialog by remember { mutableStateOf(false) }

    if (group == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text("Group not found")
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(group.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    if (group.creatorName != viewModel.currentUserName) {
                        IconButton(onClick = { showLeaveDialog = true }) {
                            Icon(Icons.Default.ExitToApp, "Leave Group")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTab) {
                0 -> GroupOverviewTab(group)
                1 -> NotesApp()
                2 -> GroupMembersTab(group, viewModel)
            }
        }
    }

    if (showLeaveDialog) {
        AlertDialog(
            onDismissRequest = { showLeaveDialog = false },
            title = { Text("Leave Group") },
            text = { Text("Are you sure you want to leave ${group.name}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.leaveGroup(groupId)
                        showLeaveDialog = false
                        onBack()
                    }
                ) {
                    Text("Leave")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLeaveDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun GroupOverviewTab(group: StudyGroup) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = group.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = group.domain,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Icon(Icons.Default.Group, contentDescription = null, modifier = Modifier.size(32.dp))
                }

                if (group.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = group.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Group Information",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        InfoCard(
            icon = Icons.Default.Person,
            title = "Created By",
            content = group.creatorName
        )

        Spacer(modifier = Modifier.height(12.dp))

        InfoCard(
            icon = Icons.Default.Group,
            title = "Members",
            content = "${group.members.size} / ${group.maxMembers} members"
        )

        Spacer(modifier = Modifier.height(12.dp))

        InfoCard(
            icon = Icons.Default.Info,
            title = "Status",
            content = if (group.members.size >= group.maxMembers) "Full" else "Open"
        )
    }
}

@Composable
fun InfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    content: String
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.padding(end = 16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun GroupMembersTab(group: StudyGroup, viewModel: StudyGroupViewModel) {
    val currentUser = viewModel.currentUserName

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Members (${group.members.size}/${group.maxMembers})",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(group.members) { member ->
                MemberCard(
                    member = member,
                    isCreator = member == group.creatorName,
                    isCurrentUser = member == currentUser
                )
            }
        }
    }
}

@Composable
fun MemberCard(
    member: String,
    isCreator: Boolean,
    isCurrentUser: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Column {
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        Text(
                            text = member,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = if (isCurrentUser) FontWeight.Bold else FontWeight.Normal
                        )
                        if (isCurrentUser) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "(You)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    if (isCreator) {
                        Text(
                            text = "Group Creator",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }

            if (isCreator) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = "Creator",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}