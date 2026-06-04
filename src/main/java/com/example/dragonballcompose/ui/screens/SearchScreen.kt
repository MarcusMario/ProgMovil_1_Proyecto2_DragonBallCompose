package com.example.dragonballcompose.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.dragonballcompose.models.Character
import com.example.dragonballcompose.ui.theme.*
import com.example.dragonballcompose.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onCharacterClick: (Int) -> Unit,
    onLogout: () -> Unit,
    viewModel: SearchViewModel = viewModel()
) {
    val characters by viewModel.characters.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    var query by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().background(BackgroundDark)
    ) {
        TopAppBar(
            title = { Text("Dragon Ball Z", color = Gold, fontWeight = FontWeight.Bold, fontSize = 20.sp) },
            actions = {
                IconButton(onClick = onLogout) {
                    Icon(Icons.Default.ExitToApp, "Cerrar sesión", tint = Gold)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = CardBackground)
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Buscar personaje...") },
                singleLine = true,
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Gold,
                    unfocusedBorderColor = TextSecondary,
                    focusedLabelColor = Gold,
                    unfocusedLabelColor = TextSecondary,
                    cursorColor = Gold,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = { if (query.isNotBlank()) viewModel.searchCharacter(query.trim()) },
                colors = ButtonDefaults.buttonColors(containerColor = Gold),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Icon(Icons.Default.Search, null, tint = Color.Black)
            }
        }

        error?.let {
            Text(it, color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp), fontSize = 14.sp)
        }

        if (isLoading) {
            Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Gold)
            }
        }

        if (!isLoading && characters.isEmpty() && error == null) {
            Box(Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                Text("Busca un personaje de Dragon Ball Z", color = TextSecondary, fontSize = 16.sp)
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(characters) { character ->
                CharacterItem(character = character, onClick = { onCharacterClick(character.id) })
            }
        }
    }
}

@Composable
fun CharacterItem(character: Character, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(width = 80.dp, height = 100.dp).clip(RoundedCornerShape(8.dp))
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(character.name, color = Gold, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Raza: ${character.race}", color = Color.White, fontSize = 14.sp)
                Text("Afiliación: ${character.affiliation}", color = TextSecondary, fontSize = 13.sp)
                Text("Ki: ${character.ki}", color = TextSecondary, fontSize = 13.sp)
            }
            Text("›", color = Gold, fontSize = 28.sp)
        }
    }
}
