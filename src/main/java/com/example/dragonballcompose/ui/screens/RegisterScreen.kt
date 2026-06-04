package com.example.dragonballcompose.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dragonballcompose.models.UserProfile
import com.example.dragonballcompose.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var apellidoPaterno by remember { mutableStateOf("") }
    var apellidoMaterno by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Gold,
        unfocusedBorderColor = TextSecondary,
        focusedLabelColor = Gold,
        unfocusedLabelColor = TextSecondary,
        cursorColor = Gold,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White
    )

    Box(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text(
                text = "Crear Cuenta",
                color = Gold, fontSize = 26.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
            )
            Text(
                text = "Ingresa tus datos para registrarte",
                color = TextSecondary, fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") },
                        singleLine = true, modifier = Modifier.fillMaxWidth(), colors = fieldColors)
                    OutlinedTextField(apellidoPaterno, { apellidoPaterno = it }, label = { Text("Apellido Paterno") },
                        singleLine = true, modifier = Modifier.fillMaxWidth(), colors = fieldColors)
                    OutlinedTextField(apellidoMaterno, { apellidoMaterno = it }, label = { Text("Apellido Materno") },
                        singleLine = true, modifier = Modifier.fillMaxWidth(), colors = fieldColors)
                    OutlinedTextField(username, { username = it }, label = { Text("Nombre de usuario") },
                        singleLine = true, modifier = Modifier.fillMaxWidth(), colors = fieldColors)
                    OutlinedTextField(email, { email = it }, label = { Text("Correo electrónico") },
                        singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(), colors = fieldColors)
                    OutlinedTextField(
                        value = password, onValueChange = { password = it },
                        label = { Text("Contraseña") }, singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null, tint = TextSecondary)
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(), colors = fieldColors
                    )
                    OutlinedTextField(
                        value = confirmPassword, onValueChange = { confirmPassword = it },
                        label = { Text("Confirmar contraseña") }, singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(), colors = fieldColors
                    )

                    errorMessage?.let {
                        Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
                    }

                    if (isLoading) {
                        CircularProgressIndicator(color = Gold, modifier = Modifier.align(Alignment.CenterHorizontally))
                    }

                    Button(
                        onClick = {
                            errorMessage = when {
                                listOf(nombre, apellidoPaterno, apellidoMaterno, username, email, password).any { it.isBlank() } ->
                                    "Completa todos los campos"
                                password != confirmPassword -> "Las contraseñas no coinciden"
                                password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
                                else -> null
                            }
                            if (errorMessage != null) return@Button

                            isLoading = true
                            auth.createUserWithEmailAndPassword(email.trim(), password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val uid = task.result.user!!.uid
                                        val profile = UserProfile(uid, nombre, apellidoPaterno, apellidoMaterno, username, email.trim())
                                        db.collection("users").document(uid).set(profile)
                                            .addOnSuccessListener { isLoading = false; onRegisterSuccess() }
                                            .addOnFailureListener { e -> isLoading = false; errorMessage = e.message }
                                    } else {
                                        isLoading = false
                                        errorMessage = task.exception?.message
                                    }
                                }
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Gold),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !isLoading
                    ) {
                        Text("Crear Cuenta", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    Text(
                        text = "¿Ya tienes cuenta? Inicia sesión",
                        color = Gold,
                        modifier = Modifier.fillMaxWidth().clickable { onNavigateToLogin() }.padding(8.dp),
                        textAlign = TextAlign.Center, fontSize = 14.sp
                    )
                }
            }
        }
    }
}
