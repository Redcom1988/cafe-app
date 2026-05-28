package com.redcom1988.cafej3.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
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
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import com.redcom1988.cafej3.screens.home.HomeScreen
import com.redcom1988.cafej3.screens.login.LoginScreen
import com.redcom1988.cafej3.theme.AppTheme
import com.redcom1988.cafej3.components.BottomNavBar
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.graphics.Color


class MainActivity : ComponentActivity() {

    private var isReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (isReady) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )

        handlePreDraw()

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

    private fun handlePreDraw() {
        // Handle pre draw here (e.g. Splash Screen, fetch data, etc)
        isReady = true
    }
}
