package com.redcom1988.cafej3.screens.cart

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.viewinterop.AndroidView
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.redcom1988.core.util.inject
import com.redcom1988.domain.order.interactor.ConfirmPayment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class PaymentWebViewScreen(
    val paymentUrl: String,
    val orderId: Int
) : Screen {

    @Suppress("unused")
    private fun readResolve(): Any = PaymentWebViewScreen(paymentUrl, orderId)

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("SetJavaScriptEnabled")
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val confirmPayment: ConfirmPayment = inject()
        val scope = remember { CoroutineScope(Dispatchers.Main) }
        var isLoading by remember { mutableStateOf(true) }
        var showBackDialog by remember { mutableStateOf(false) }
        var paymentConfirmed by remember { mutableStateOf(false) }

        if (showBackDialog) {
            AlertDialog(
                onDismissRequest = { showBackDialog = false },
                title = { Text("Cancel Payment?") },
                text = { Text("Your order will remain unpaid. You can pay later from My Order.") },
                confirmButton = {
                    TextButton(onClick = {
                        showBackDialog = false
                        navigator.pop()
                    }) {
                        Text("Leave")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showBackDialog = false }) {
                        Text("Continue Payment")
                    }
                }
            )
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Payment", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { showBackDialog = true }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            settings.loadWithOverviewMode = true
                            settings.useWideViewPort = true
                            webViewClient = object : WebViewClient() {
                                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                    isLoading = true
                                }

                                override fun onPageFinished(view: WebView?, url: String?) {
                                    isLoading = false
                                }

                                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                                    val url = request?.url.toString() ?: ""

                                    if (!paymentConfirmed && url.contains("payment-finish")) {
                                        paymentConfirmed = true
                                        scope.launch {
                                            try {
                                                confirmPayment.await(orderId)
                                            } catch (_: Exception) { }
                                        }
                                        navigator.pop()
                                        return true
                                    }

                                    // Block non-http URLs (intent://, etc.) to prevent leaving the app
                                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                                        return true
                                    }

                                    return false
                                }
                            }
                            loadUrl(paymentUrl)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
