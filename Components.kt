package com.lpu.collegeinformationapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class TabItem(val title: String, val icon: ImageVector)

@Composable
fun SectionHeader(title: String) {
    Text(text = title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(12.dp))
}

@Composable
fun InfoCard(
    title: String,
    subtitle: String,
    description: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun FacultyCard(name: String, department: String, designation: String, email: String, phone: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(56.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary), contentAlignment = Alignment.Center) {
                Text(name.split(" ").joinToString("") { it.first().toString() }.take(2), color = Color.White)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("$designation, $department", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(6.dp))
                Text(email, style = MaterialTheme.typography.bodySmall)
                Text(phone, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun EventCard(name: String, date: String, venue: String, description: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("$date • $venue", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun DrawerContent(
    onHomeSelected: () -> Unit,
    onAboutSelected: () -> Unit,
    onContactSelected: () -> Unit,
    onSettingsSelected: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
        Text("College Information", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))
        NavigationDrawerItem(label = { Text("Home") }, selected = false, onClick = onHomeSelected, icon = { Icon(Icons.Default.Home, contentDescription = "Home") })
        NavigationDrawerItem(label = { Text("About College") }, selected = false, onClick = onAboutSelected, icon = { Icon(Icons.Default.Info, contentDescription = "About") })
        NavigationDrawerItem(label = { Text("Contact Us") }, selected = false, onClick = onContactSelected, icon = { Icon(Icons.Default.Call, contentDescription = "Contact") })
        NavigationDrawerItem(label = { Text("Settings") }, selected = false, onClick = onSettingsSelected, icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") })
    }
}
