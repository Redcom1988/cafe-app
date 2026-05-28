package com.redcom1988.cafej3.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.redcom1988.cafej3.R
import com.redcom1988.cafej3.components.Header
import com.redcom1988.cafej3.model.UserProfile
import com.redcom1988.cafej3.theme.*
import com.redcom1988.cafej3.viewmodel.ProfileViewModel
import com.redcom1988.cafej3.screens.profile.components.*

object ProfileScreen : Tab {

    private fun readResolve(): Any = ProfileScreen

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Outlined.PersonOutline)
            return remember { TabOptions(index = 3u, title = "Profile", icon = icon) }
        }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = viewModel<ProfileViewModel>()
        val profile = viewModel.profile.value

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
        ) {
                item {
                    Header()

                    Spacer(modifier = Modifier.height(16.dp))

                    ProfileCard(profile = profile)

                    Spacer(modifier = Modifier.height(16.dp))

                    ProfileMenuCard(
                        icon = { Icon(Icons.Outlined.PersonOutline, null, tint = Orange2, modifier = Modifier.size(22.dp)) },
                        title = "Edit Profile",
                        subtitle = "Update your personal information",
                        onClick = { navigator.push(EditProfileScreen(viewModel)) }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    ProfileMenuCard(
                        icon = { Icon(Icons.Outlined.ReceiptLong, null, tint = Orange2, modifier = Modifier.size(22.dp)) },
                        title = "Order History",
                        subtitle = "View all past receipts and orders",
                        onClick = { navigator.push(OrderHistoryScreen) }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    ProfileMenuCard(
                        icon = { Icon(Icons.Outlined.Settings, null, tint = Orange2, modifier = Modifier.size(22.dp)) },
                        title = "App Settings",
                        subtitle = "Notifications, privacy, and theme",
                        onClick = {}
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    ProfileMenuCard(
                        icon = { Icon(Icons.Outlined.HelpOutline, null, tint = Orange2, modifier = Modifier.size(22.dp)) },
                        title = "Help Center",
                        subtitle = "Contact support and FAQs",
                        onClick = {}
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    LogoutButton(onClick = {})

                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        }

    @Composable
    fun ProfileCard(profile: UserProfile) {
        Column(
            modifier = Modifier
                .padding(horizontal = 22.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(34.dp))
                .background(White)
                .padding(vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Image(
                    painter = painterResource(id = R.drawable.profile2),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Orange2),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Verified, contentDescription = null, tint = White, modifier = Modifier.size(14.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(text = profile.name, color = DarkBrown, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Member since ${profile.memberSince}", color = SoftBrown, fontSize = 13.sp)
        }
    }

    @Composable
    fun ProfileMenuCard(
        icon: @Composable () -> Unit,
        title: String,
        subtitle: String,
        onClick: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 22.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp))
                .background(White)
                .clickable { onClick() }
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Cream),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = DarkBrown, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = subtitle, color = SoftBrown, fontSize = 12.sp)
            }
            Icon(Icons.Outlined.ArrowForwardIos, contentDescription = null, tint = SoftBrown, modifier = Modifier.size(16.dp))
        }
    }

    @Composable
    fun LogoutButton(onClick: () -> Unit) {
        Row(
            modifier = Modifier
                .padding(horizontal = 22.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(50.dp))
                .background(PrimaryOrange)
                .clickable { onClick() }
                .padding(vertical = 14.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Outlined.Logout, contentDescription = null, tint = White, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Log Out", color = White, fontWeight = FontWeight.Medium, fontSize = 16.sp)
        }
    }
}