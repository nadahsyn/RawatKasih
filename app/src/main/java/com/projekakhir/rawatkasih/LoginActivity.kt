package com.projekakhir.rawatkasih

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.projekakhir.rawatkasih.data.AppUser
import com.projekakhir.rawatkasih.screen.CaregiverHomeScreen
import com.projekakhir.rawatkasih.screens.*
import com.projekakhir.rawatkasih.ui.theme.RawatKasihTheme
import com.projekakhir.rawatkasih.viewmodel.ViewModelFactory
import kotlinx.serialization.Serializable

@Serializable object LoginDest
@Serializable object RegisterDest
@Serializable data class PatientHomeDest(val userId: Long)
@Serializable data class CaregiverHomeDest(val userId: Long, val name: String)
@Serializable data class EditProfileDest(val userId: Long)
@Serializable data class HealthDest(val userId: Long)
@Serializable data class EditHealthProfileDest(val userId: Long)
@Serializable data class HealthHistoryDest(val userId: Long)

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RawatKasihTheme {
                RawatKasihNavHost()
            }
        }
    }
}

@Composable
private fun RawatKasihNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val app = context.applicationContext as RawatKasihApplication
    val factory = ViewModelFactory(app.repository)

    NavHost(
        navController = navController,
        startDestination = LoginDest
    ) {
        composable<LoginDest> {
            LoginScreen(
                viewModel = viewModel(factory = factory),
                onNavigateToRegister = { navController.navigate(RegisterDest) },
                onLoginSuccess = { result ->
                    val user = result.user
                    if (user.role == "caregiver") {
                        navController.navigate(CaregiverHomeDest(user.id!!, user.name)) {
                            popUpTo(LoginDest) { inclusive = true }
                        }
                    } else {
                        navController.navigate(PatientHomeDest(user.id!!)) {
                            popUpTo(LoginDest) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable<RegisterDest> {
            RegisterScreen(
                viewModel = viewModel(factory = factory),
                onNavigateBack = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(LoginDest) {
                        popUpTo(RegisterDest) { inclusive = true }
                    }
                }
            )
        }

        composable<PatientHomeDest> { backStackEntry ->
            val dest = backStackEntry.toRoute<PatientHomeDest>()
            PatientHomeScreen(
                userId = dest.userId,
                viewModel = viewModel(factory = factory),
                onEditProfile = { id -> navController.navigate(EditProfileDest(id)) },
                onOpenHealth = { id -> navController.navigate(HealthDest(id)) },
                onLogout = {
                    navController.navigate(LoginDest) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                successMessage = null,
                onMessageShown = {}
            )
        }

        composable<CaregiverHomeDest> { backStackEntry ->
            val dest = backStackEntry.toRoute<CaregiverHomeDest>()
            CaregiverHomeScreen(
                user = AppUser(id = dest.userId, name = dest.name, role = "caregiver"),
                viewModel = viewModel(factory = factory),
                onLogout = {
                    navController.navigate(LoginDest) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable<EditProfileDest> { backStackEntry ->
            val dest = backStackEntry.toRoute<EditProfileDest>()
            EditProfileScreen(
                userId = dest.userId,
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(LoginDest) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onProfileUpdated = { navController.popBackStack() }
            )
        }

        composable<HealthDest> { backStackEntry ->
            val dest = backStackEntry.toRoute<HealthDest>()
            HealthScreen(
                userId = dest.userId,
                viewModel = viewModel(factory = factory),
                onBack = { navController.popBackStack() },
                onEditHealthProfile = { id -> navController.navigate(EditHealthProfileDest(id)) },
                onOpenHistory = { id -> navController.navigate(HealthHistoryDest(id)) },
                successMessage = null,
                onMessageShown = {}
            )
        }

        composable<EditHealthProfileDest> { backStackEntry ->
            val dest = backStackEntry.toRoute<EditHealthProfileDest>()
            EditHealthProfileScreen(
                userId = dest.userId,
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(LoginDest) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onProfileSaved = { navController.popBackStack() }
            )
        }

        composable<HealthHistoryDest> { backStackEntry ->
            val dest = backStackEntry.toRoute<HealthHistoryDest>()
            HealthHistoryScreen(
                userId = dest.userId,
                viewModel = viewModel(factory = factory),
                onBack = { navController.popBackStack() }
            )
        }
    }
}