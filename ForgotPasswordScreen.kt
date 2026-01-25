package com.example.studee

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions

@Composable
fun ForgotPasswordScreen(
    onBack: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // TITLE
            Text(
                text = "Reset Password",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(20.dp))

            // EMAIL INPUT
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    error = ""
                    message = ""
                },
                label = { Text("Enter your email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = error.isNotEmpty()
            )

            if (error.isNotEmpty()) {
                Text(text = error, color = Color.Red, modifier = Modifier.align(Alignment.Start))
            }

            if (message.isNotEmpty()) {
                Text(text = message, color = Color(0xFF0A7D00), modifier = Modifier.align(Alignment.Start))
            }

            Spacer(Modifier.height(20.dp))

            // SEND RESET LINK
            Button(
                onClick = {
                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        message = "Password reset link sent to $email"
                    } else {
                        error = "Please enter a valid email"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Send Reset Link")
            }

            Spacer(Modifier.height(20.dp))

            // BACK BUTTON
            TextButton(onClick = onBack) {
                Text("Back")
            }
        }
    }
}