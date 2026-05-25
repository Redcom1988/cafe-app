package com.redcom1988.cafej3.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.redcom1988.cafej3.R
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.redcom1988.cafej3.screens.scan.ScanScreen
import com.redcom1988.cafej3.theme.*


object LoginScreen : Screen {

    private fun readResolve(): Any = LoginScreen

    @Composable
    override fun Content() {

        var selectedTab by remember { mutableStateOf("Phone") }
        var phoneNumber by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        val navigator = LocalNavigator.currentOrThrow

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                // HEADER IMAGE
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                ) {

                    Image(
                        painter = painterResource(R.drawable.coffee_banner),
                        contentDescription = "Coffee Banner",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // GRADIENT OVERLAY
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        backgroundColor
                                    )
                                )
                            )
                    )
                }

                // CONTENT
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Welcome back to\nCafe Jasjisjus",
                        color = brownPrimary,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 38.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Sign in to earn rewards and view your favorites.",
                        color = brownDark,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    // LOGIN CARD
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(32.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = White
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 10.dp
                        )
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {

                            // TAB SWITCH
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(softPink)
                                    .padding(4.dp)
                            ) {

                                // PHONE TAB
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(
                                            if (selectedTab == "Phone")
                                                White
                                            else
                                                Color.Transparent
                                        )
                                        .clickable {
                                            selectedTab = "Phone"
                                        }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {

                                    Text(
                                        text = "Phone",
                                        color = if (selectedTab == "Phone")
                                            brownPrimary
                                        else
                                            brownDark,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                // EMAIL TAB
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(
                                            if (selectedTab == "Email")
                                                White
                                            else
                                                Color.Transparent
                                        )
                                        .clickable {
                                            selectedTab = "Email"
                                        }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {

                                    Text(
                                        text = "Email",
                                        color = if (selectedTab == "Email")
                                            brownPrimary
                                        else
                                            brownDark,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(28.dp))

                            // INPUT LABEL
                            Text(
                                text = if (selectedTab == "Phone")
                                    "Phone Number"
                                else
                                    "Email Address",
                                color = brownDark,
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // INPUT FIELD
                            OutlinedTextField(
                                value = if (selectedTab == "Phone")
                                    phoneNumber
                                else
                                    email,
                                onValueChange = {
                                    if (selectedTab == "Phone") {
                                        phoneNumber = it
                                    } else {
                                        email = it
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(70.dp),
                                placeholder = {

                                    Text(
                                        text = if (selectedTab == "Phone")
                                            "+1 (555) 000-0000"
                                        else
                                            "example@email.com",
                                        color = grayText
                                    )
                                },
                                shape = RoundedCornerShape(20.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = inputColor,
                                    unfocusedContainerColor = inputColor,
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent
                                ),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = if (selectedTab == "Phone")
                                        KeyboardType.Phone
                                    else
                                        KeyboardType.Email
                                ),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            // SIGN IN BUTTON
                            Button(
                                onClick = {
                                    navigator.push(ScanScreen)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(65.dp),
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = brownPrimary
                                )
                            ) {

                                Text(
                                    text = "Sign In",
                                    color = White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "New to the cafe? ",
                            color = brownDark,
                            fontSize = 18.sp
                        )

                        Text(
                            text = "Create an account",
                            color = brownPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable {

                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = "Help Center",
                            color = grayText,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.width(24.dp))

                        Text(
                            text = "Privacy Policy",
                            color = grayText,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}