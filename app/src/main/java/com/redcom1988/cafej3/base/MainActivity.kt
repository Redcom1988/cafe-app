package com.redcom1988.cafej3.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
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
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import soup.compose.material.motion.animation.materialSharedAxisX
import soup.compose.material.motion.animation.rememberSlideDistance

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val networkPreference = inject<NetworkPreference>()
        val token = networkPreference.accessToken().get()
        val initialScreen: Screen = if (token.isNotBlank()) MainScreen() else LoginScreen

        enableEdgeToEdge()
        setContent {
            AppTheme {
                val slideDistance = rememberSlideDistance()
                Navigator(
                    screen = initialScreen,
                    disposeBehavior = NavigatorDisposeBehavior(
                        disposeNestedNavigators = false,
                        disposeSteps = true,
                    )
                ) { navigator ->
                    ScreenTransition(
                        modifier = Modifier.fillMaxSize(),
                        navigator = navigator,
                        transition = {
                            materialSharedAxisX(
                                forward = navigator.lastEvent != StackEvent.Pop,
                                slideDistance = slideDistance,
                            )
                        },
                    )
                    HandleNewIntent(this@MainActivity, navigator)
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
            }.collectLatest { handleIntentAction(it, navigator) }
        }
    }

    private fun handleIntentAction(intent: Intent, navigator: Navigator) {
    }
}
