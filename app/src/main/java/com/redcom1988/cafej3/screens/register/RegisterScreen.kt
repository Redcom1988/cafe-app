package com.redcom1988.cafej3.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.redcom1988.cafej3.screens.main.MainScreen
import com.redcom1988.cafej3.components.PrimaryButton

data object RegisterScreen : Screen {

    @Suppress("unused")
    private fun readResolve(): Any = RegisterScreen

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = rememberScreenModel { RegisterScreenModel() }
        val state by screenModel.state.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Create Account",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Join Cafe Jasjisjus and earn rewards",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            FieldLabel("Username")
            OutlinedTextField(
                value = state.username,
                onValueChange = screenModel::onUsernameChange,
                placeholder = { Text("Choose a username") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            FieldLabel("Full Name")
            OutlinedTextField(
                value = state.name,
                onValueChange = screenModel::onNameChange,
                placeholder = { Text("Your full name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            FieldLabel("Email")
            OutlinedTextField(
                value = state.email,
                onValueChange = screenModel::onEmailChange,
                placeholder = { Text("email@example.com") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            FieldLabel("Password")
            OutlinedTextField(
                value = state.password,
                onValueChange = screenModel::onPasswordChange,
                placeholder = { Text("Create a password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors(),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            FieldLabel("Phone (optional)")
            OutlinedTextField(
                value = state.phoneNumber,
                onValueChange = screenModel::onPhoneChange,
                placeholder = { Text("08xxxxxxxxxx") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (!state.error.isNullOrBlank()) {
                Text(
                    text = state.error ?: "",
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            PrimaryButton(
                text = if (state.isLoading) "Creating Account..." else "Create Account",
                onClick = {
                    screenModel.submit {
                        navigator.replace(MainScreen())
                    }
                },
                enabled = !state.isLoading &&
                    state.username.isNotBlank() &&
                    state.email.isNotBlank() &&
                    state.password.length >= 6 &&
                    state.name.isNotBlank()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Already have an account? Sign In",
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                modifier = Modifier.clickable { navigator.pop() }
            )
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
    )
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    unfocusedBorderColor = Color.Transparent,
    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
)
