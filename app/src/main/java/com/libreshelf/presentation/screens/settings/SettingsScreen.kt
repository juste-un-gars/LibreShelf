package com.libreshelf.presentation.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Paramètres") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                SettingsSection(title = "Apparence")
            }
            item {
                SettingsItem(
                    icon = Icons.Default.Palette,
                    title = "Thème",
                    subtitle = "Automatique",
                    onClick = { /* TODO */ }
                )
            }
            item {
                Divider()
            }

            item {
                SettingsSection(title = "Stockage")
            }
            item {
                SettingsItem(
                    icon = Icons.Default.Folder,
                    title = "Dossier par défaut",
                    subtitle = "/storage/emulated/0/Books",
                    onClick = { /* TODO */ }
                )
            }
            item {
                SettingsItem(
                    icon = Icons.Default.CloudUpload,
                    title = "Sauvegarde",
                    subtitle = "Sauvegarder la bibliothèque",
                    onClick = { /* TODO */ }
                )
            }
            item {
                Divider()
            }

            item {
                SettingsSection(title = "Réseau")
            }
            item {
                SettingsItem(
                    icon = Icons.Default.NetworkCheck,
                    title = "Sources réseau",
                    subtitle = "Gérer les connexions SMB, WebDAV, FTP",
                    onClick = { /* TODO */ }
                )
            }
            item {
                Divider()
            }

            item {
                SettingsSection(title = "À propos")
            }
            item {
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = "Version",
                    subtitle = "1.0.0",
                    onClick = { /* TODO */ }
                )
            }
            item {
                SettingsItem(
                    icon = Icons.Default.Code,
                    title = "Code source",
                    subtitle = "github.com/juste-un-gars/LibreShelf",
                    onClick = { /* TODO: Open URL */ }
                )
            }
        }
    }
}

@Composable
fun SettingsSection(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
    }
}
