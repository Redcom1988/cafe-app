package com.redcom1988.cafej3.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.util.Consumer
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import cafe.adriel.voyager.transitions.ScreenTransition
import com.redcom1988.cafej3.screens.main.MainScreen
import com.redcom1988.cafej3.theme.AppTheme
import com.redcom1988.cafej3.screens.login.LoginScreen
import com.redcom1988.core.util.inject
import com.redcom1988.core.network.NetworkPreference
import com.redcom1988.domain.table.interactor.TableSession
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.graphics.Color


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val networkPreference = inject<NetworkPreference>()
        val tableSession = inject<TableSession>()
        val token = networkPreference.accessToken().get()
        val hasActiveGuestOrder = tableSession.hasTrackingToken()

        val initialScreen: Screen = when {
            token.isNotBlank() -> MainScreen(isGuest = false)
            hasActiveGuestOrder -> MainScreen(isGuest = true)
            else -> LoginScreen
        }

        enableEdgeToEdge()
        setContent {
            AppTheme {
                TabNavigator(HomeScreen) { tabNavigator ->
                    Scaffold(
                        containerColor = Color.White,
                        bottomBar = {
                            BottomNavBar(tabNavigator = tabNavigator)
                        }
                    ) { padding ->
                        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
                            CurrentTab()
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun HandleNewIntent(context: Context, navigator: Navigator) {
        LaunchedEffect(Unit) {
            callbackFlow {
                val componentActivity = context as ComponentActivity
                val consumer = Consumer<Intent> { trySend(it) }
                componentActivity.addOnNewIntentListener(consumer)
                awaitClose { componentActivity.removeOnNewIntentListener(consumer) }
            }.collectLatest { handleIntentAction() }
        }
    }

    private fun handleIntentAction() {
    }
}
