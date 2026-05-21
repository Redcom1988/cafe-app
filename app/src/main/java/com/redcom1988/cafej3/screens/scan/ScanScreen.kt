package com.redcom1988.cafej3.screens.scan

import android.Manifest
import android.annotation.SuppressLint
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FlashlightOn
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import cafe.adriel.voyager.core.screen.Screen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import androidx.compose.material3.Button
import com.redcom1988.cafej3.screens.home.HomeScreen
import cafe.adriel.voyager.navigator.LocalNavigator

object ScanScreen : Screen {

    private fun readResolve(): Any = ScanScreen

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    override fun Content() {

        // COLOR PALETTE
        val backgroundColor = Color(0xFFFBF3E4)
        val brownPrimary = Color(0xFF9E3D00)
        val brownDark = Color(0xFF574239)
        val grayText = Color(0xFFA8A29E)
        val white = Color(0xFFFFFFFF)
        Color(0xFFFF6B1A)
        val blueTip = Color(0xFF1D4FA3)

        var scannedResult by remember {
            mutableStateOf("")
        }

        val cameraPermissionState =
            rememberPermissionState(
                Manifest.permission.CAMERA
            )
        val navigator = LocalNavigator.current

        LaunchedEffect(Unit) {
            cameraPermissionState.launchPermissionRequest()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(48.dp))

                // TITLE
                Text(
                    text = "Scan to Check-in",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF231815),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Point your camera at the QR code on your table to start ordering.",
                    fontSize = 18.sp,
                    color = brownDark,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // CAMERA SCANNER
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .width(90.dp)
                        .clip(RoundedCornerShape(30.dp))
                ) {

                    if (cameraPermissionState.status.isGranted) {

                        CameraPreview(
                            modifier = Modifier.fillMaxSize(),
                            onQrCodeScanned = {
                                scannedResult = it
                            }
                        )

                    } else {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.DarkGray),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = "Camera permission denied",
                                color = white
                            )
                        }
                    }

                    // FLASH BUTTON
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(18.dp)
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(
                                Color.White.copy(alpha = 0.30f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Outlined.FlashlightOn,
                            contentDescription = "Flash",
                            tint = white,
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    // SCANNER FRAME
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(48.dp)
//                    ) {
//
//                        ScannerCorner(
//                            modifier = Modifier.align(Alignment.TopStart),
//                            color = orangeScanner
//                        )
//
//                        ScannerCorner(
//                            modifier = Modifier.align(Alignment.TopEnd),
//                            color = orangeScanner,
//                            rotate = true
//                        )
//
//                        ScannerCorner(
//                            modifier = Modifier.align(Alignment.BottomStart),
//                            color = orangeScanner,
//                            flipVertical = true
//                        )
//
//                        ScannerCorner(
//                            modifier = Modifier.align(Alignment.BottomEnd),
//                            color = orangeScanner,
//                            rotate = true,
//                            flipVertical = true
//                        )
//                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (scannedResult.isNotEmpty()) {

                    Text(
                        text = "Scanned QR: $scannedResult",
                        color = brownPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }

                // MANUAL ENTRY BUTTON
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navigator?.push(HomeScreen)
                        },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = white
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 28.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Outlined.Keyboard,
                            contentDescription = "Keyboard",
                            tint = brownPrimary,
                            modifier = Modifier.size(28.dp)
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Text(
                            text = "Manual Entry",
                            fontSize = 22.sp,
                            color = brownPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // TROUBLE SCANNING
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Info",
                        tint = grayText,
                        modifier = Modifier.size(28.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Having trouble scanning?",
                        fontSize = 18.sp,
                        color = grayText
                    )
                }

                Spacer(modifier = Modifier.height(180.dp))

                // PRO TIP CARD
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(30.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = white
                    )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(
                                    Color(0xFFE7F0FF)
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                imageVector = Icons.Outlined.Lightbulb,
                                contentDescription = "Tip",
                                tint = blueTip,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(20.dp))

                        Column {

                            Text(
                                text = "Pro Tip",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF231815)
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Hold your phone about 6 inches from the code.",
                                fontSize = 18.sp,
                                color = brownDark,
                                lineHeight = 28.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ScannerCorner(
    modifier: Modifier = Modifier,
    color: Color,
    rotate: Boolean = false,
    flipVertical: Boolean = false
) {

    Box(
        modifier = modifier.size(90.dp)
    ) {

        Box(
            modifier = Modifier
                .width(90.dp)
                .height(10.dp)
                .background(
                    color,
                    RoundedCornerShape(10.dp)
                )
        )

        Box(
            modifier = Modifier
                .width(10.dp)
                .height(90.dp)
                .background(
                    color,
                    RoundedCornerShape(10.dp)
                )
        )
    }
}

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    onQrCodeScanned: (String) -> Unit
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        modifier = modifier,
        factory = {

            val previewView = PreviewView(context)

            val cameraProviderFuture =
                ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({

                val cameraProvider =
                    cameraProviderFuture.get()

                val preview = Preview.Builder().build()

                preview.surfaceProvider = previewView.surfaceProvider

                val barcodeScanner =
                    BarcodeScanning.getClient()

                val imageAnalysis =
                    ImageAnalysis.Builder()
                        .setBackpressureStrategy(
                            ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
                        )
                        .build()

                imageAnalysis.setAnalyzer(
                    ContextCompat.getMainExecutor(context)
                ) { imageProxy ->

                    processImageProxy(
                        barcodeScanner,
                        imageProxy,
                        onQrCodeScanned
                    )
                }

                val cameraSelector =
                    CameraSelector.DEFAULT_BACK_CAMERA

                try {

                    cameraProvider.unbindAll()

                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }, ContextCompat.getMainExecutor(context))

            previewView
        }
    )
}

@androidx.annotation.OptIn(ExperimentalGetImage::class)
private fun processImageProxy(
    barcodeScanner: com.google.mlkit.vision.barcode.BarcodeScanner,
    imageProxy: ImageProxy,
    onQrCodeScanned: (String) -> Unit
) {

    val mediaImage = imageProxy.image

    if (mediaImage != null) {

        val image = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )

        barcodeScanner.process(image)
            .addOnSuccessListener { barcodes ->

                for (barcode in barcodes) {

                    barcode.rawValue?.let {
                        onQrCodeScanned(it)
                    }
                }
            }
            .addOnCompleteListener {
                imageProxy.close()
            }

    } else {
        imageProxy.close()
    }
}