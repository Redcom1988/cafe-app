package com.redcom1988.cafej3.screens.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.redcom1988.cafej3.model.UserProfile
import com.redcom1988.cafej3.theme.*
import com.redcom1988.cafej3.viewmodel.ProfileViewModel

class EditProfileScreen(private val sharedViewModel: ProfileViewModel) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val current = sharedViewModel.profile.value

        var name by remember { mutableStateOf(current.name) }
        var email by remember { mutableStateOf(current.email) }
        var phone by remember { mutableStateOf(current.phone) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navigator.pop() }) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBackIosNew,
                        contentDescription = "Back",
                        tint = DarkBrown
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Edit Profile",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DarkBrown
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Column(
                modifier = Modifier
                    .padding(horizontal = 22.dp)
                    .fillMaxWidth()
            ) {
                ProfileTextField(label = "Full Name", value = name, onValueChange = { name = it })
                Spacer(modifier = Modifier.height(10.dp))
                ProfileTextField(label = "Email", value = email, onValueChange = { email = it })
                Spacer(modifier = Modifier.height(10.dp))
                ProfileTextField(label = "Phone", value = phone, onValueChange = { phone = it })
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        sharedViewModel.updateProfile(
                            UserProfile(name = name, email = email, phone = phone)
                        )
                        navigator.pop()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text(
                        text = "Save Changes",
                        fontSize = 15.sp,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(text = label, color = SoftBrown, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryOrange,
                unfocusedBorderColor = Color.LightGray,
                focusedContainerColor = White,
                unfocusedContainerColor = White
            )
        )
    }
}