package com.projekakhir.rawatkasih

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.projekakhir.rawatkasih.data.AuthResult
import com.projekakhir.rawatkasih.screen.CaregiverHomeScreen
import com.projekakhir.rawatkasih.screens.EditHealthProfileScreen
import com.projekakhir.rawatkasih.screens.PatientHomeScreen
import com.projekakhir.rawatkasih.ui.theme.RawatKasihTheme
import com.projekakhir.rawatkasih.screens.EditProfileScreen
import com.projekakhir.rawatkasih.screens.HealthScreen
import com.projekakhir.rawatkasih.screens.HealthHistoryScreen

private sealed interface AppRoute {
    data object Login : AppRoute
    data object Register : AppRoute
    data class Home(val session: AuthResult) : AppRoute
    data class EditProfile(val session: AuthResult) : AppRoute
    data class Health(val session: AuthResult) : AppRoute
    data class EditHealthProfile(val session: AuthResult) : AppRoute
    data class HealthHistory(
        val session: AuthResult
    ) : AppRoute

}

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RawatKasihTheme {
                RawatKasihApp()
            }
        }
    }
}

@Composable
private fun RawatKasihApp() {
    var successMessage by remember {
        mutableStateOf<String?>(null)
    }
    var route by remember { mutableStateOf<AppRoute>(AppRoute.Login) }

    when (val currentRoute = route) {
        AppRoute.Login -> LoginScreen(
            onNavigateToRegister = { route = AppRoute.Register },
            onLoginSuccess = { route = AppRoute.Home(it) }
        )

        AppRoute.Register -> RegisterScreen(
            onNavigateBack = { route = AppRoute.Login },
            onRegisterSuccess = { route = AppRoute.Login }
        )

        is AppRoute.Home -> {
            if (currentRoute.session.user.role == "caregiver") {
                CaregiverHomeScreen(user = currentRoute.session.user)
            } else {
                PatientHomeScreen(
                    initialSession = currentRoute.session,
                    successMessage = successMessage,
                    onMessageShown = {
                        successMessage = null
                    },
                    onEditProfile = {
                        route = AppRoute.EditProfile(
                            currentRoute.session
                        )
                    },
                    onOpenHealth = {
                        route = AppRoute.Health(
                            currentRoute.session
                        )
                    }
                )
            }
        }

        is AppRoute.EditProfile -> {
            EditProfileScreen(
                user = currentRoute.session.user,

                onBack = {
                    route = AppRoute.Home(
                        currentRoute.session
                    )
                },

                onProfileUpdated = { updatedUser ->

                    successMessage = "Perubahan profil berhasil disimpan"

                    route = AppRoute.Home(
                        currentRoute.session.copy(
                            user = updatedUser
                        )
                    )
                }
            )
        }
        is AppRoute.Health -> {
            HealthScreen(
                userId = currentRoute.session.user.id!!,

                successMessage = successMessage,

                onMessageShown = {
                    successMessage = null
                },

                onBack = {
                    route = AppRoute.Home(
                        currentRoute.session
                    )
                },

                onEditHealthProfile = {
                    route = AppRoute.EditHealthProfile(
                        currentRoute.session
                    )
                },

                onOpenHistory = {
                    route = AppRoute.HealthHistory(
                        currentRoute.session
                    )
                }
            )
        }
        is AppRoute.EditHealthProfile -> {

            EditHealthProfileScreen(
                userId = currentRoute.session.user.id!!,

                onBack = {
                    route = AppRoute.Health(
                        currentRoute.session
                    )
                },

                onProfileSaved = {

                    successMessage =
                        "Profil kesehatan berhasil diperbarui!"

                    route = AppRoute.Health(
                        currentRoute.session
                    )
                }
            )
        }
        is AppRoute.HealthHistory -> {

            HealthHistoryScreen(
                userId = currentRoute.session.user.id!!,

                onBack = {
                    route = AppRoute.Health(
                        currentRoute.session
                    )
                }
            )
        }
    }
}
