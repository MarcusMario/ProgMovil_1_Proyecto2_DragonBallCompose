package com.example.dragonballcompose.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.dragonballcompose.ui.theme.*
import com.example.dragonballcompose.viewmodel.DetailViewModel
import com.example.dragonballcompose.viewmodel.DetailViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    characterId: Int,
    onBack: () -> Unit,
    viewModel: DetailViewModel = viewModel(factory = DetailViewModelFactory(characterId))
) {
    val character by viewModel.character.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().background(BackgroundDark)
    ) {
        TopAppBar(
            title = { Text(character?.name ?: "Personaje", color = Gold, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, "Regresar", tint = Gold)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = CardBackground)
        )

        when {
            isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Gold)
                }
            }
            error != null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(error ?: "", color = MaterialTheme.colorScheme.error)
                }
            }
            character != null -> {
                val char = character!!
                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Imagen del personaje
                    AsyncImage(
                        model = char.image,
                        contentDescription = char.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(width = 220.dp, height = 300.dp)
                            .padding(bottom = 16.dp)
                    )

                    // Card con toda la información
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(20.dp)
                        ) {
                            // Nombre
                            Text(
                                text = char.name,
                                color = Gold,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            // Datos del personaje
                            InfoRow("Raza", char.race)
                            InfoRow("Género", char.gender)
                            InfoRow("Ki", char.ki)
                            InfoRow("Ki Máximo", char.maxKi)
                            InfoRow("Afiliación", char.affiliation)
                            InfoRow("Planeta de Origen", char.originPlanet?.name ?: "Desconocido")

                            // Separador
                            HorizontalDivider(
                                color = TextSecondary.copy(alpha = 0.3f),
                                modifier = Modifier.padding(vertical = 16.dp)
                            )

                            // Descripción
                            Text(
                                text = "Descripción",
                                color = Gold,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = char.description,
                                color = TextSecondary,
                                fontSize = 14.sp,
                                lineHeight = 22.sp
                            )

                            // Transformaciones si tiene
                            if (!char.transformations.isNullOrEmpty()) {
                                HorizontalDivider(
                                    color = TextSecondary.copy(alpha = 0.3f),
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                                Text(
                                    text = "Transformaciones (${char.transformations.size})",
                                    color = Gold,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                char.transformations.forEach { trans ->
                                    Text(
                                        text = "• ${trans.name} — Ki: ${trans.ki}",
                                        color = TextSecondary,
                                        fontSize = 13.sp,
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(text = "$label:", color = TextSecondary, fontSize = 14.sp, modifier = Modifier.weight(0.45f))
        Text(text = value, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp,
            fontWeight = FontWeight.Medium, modifier = Modifier.weight(0.55f))
    }
}
