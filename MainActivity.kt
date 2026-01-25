package com.example.studee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.studee.ui.theme.StudeeTheme
import com.google.android.gms.auth.api.signin.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val googleClient = getGoogleClient()

        setContent {
            StudeeTheme {

                val navController = rememberNavController()
                val viewModel: StudyGroupViewModel = viewModel()

                val googleLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    if (task.isSuccessful) {
                        navController.navigate("userinfo")
                    } else {
                        println("Google Sign-In Failed: ${task.exception}")
                    }
                }

                NavHost(navController, startDestination = "login") {

                    composable("login") {
                        LoginScreen(
                            onValidEmailLogin = { _, _ ->
                                navController.navigate("userinfo")
                            },
                            onGoogleLogin = {
                                googleLauncher.launch(googleClient.signInIntent)
                            },
                            onForgotPassword = {
                                navController.navigate("forgot_password")
                            }
                        )
                    }

                    composable("forgot_password") {
                        ForgotPasswordScreen(
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable("userinfo") {
                        UserInfoScreen(
                            onSubmit = { _, _, _ ->
                                navController.navigate("dashboard")
                            }
                        )
                    }

                    composable("dashboard") {
                        DashboardScreen(
                            viewModel = viewModel,
                            onCreateNew = { navController.navigate("create_group") }
                        )
                    }

                    composable("create_group") {
                        CreateStudyGroupScreen(
                            onSubmit = { name, _, _, _ ->
                                viewModel.addGroup(name)
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }

    private fun getGoogleClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GOOGLE_SIGN_IN)
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(this, gso)
    }
}