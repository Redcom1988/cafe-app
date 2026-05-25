package com.redcom1988.cafej3.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.redcom1988.cafej3.components.*
import com.redcom1988.cafej3.theme.*

// ================= SCREEN =================

@Composable
fun ProfileScreen(
    onEditProfileClick: () -> Unit = {},
    onOrderHistoryClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onHelpCenterClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        item {

            HeaderSection()

            Spacer(modifier = Modifier.height(34.dp))

            ProfileCard()

            Spacer(modifier = Modifier.height(36.dp))

            ProfileMenuCard(
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.PersonOutline,
                        contentDescription = null,
                        tint = Orange2,
                        modifier = Modifier.size(34.dp)
                    )
                },
                title = "Edit Profile",
                subtitle = "Update your personal information",
                onClick = onEditProfileClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            ProfileMenuCard(
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.ReceiptLong,
                        contentDescription = null,
                        tint = Orange2,
                        modifier = Modifier.size(34.dp)
                    )
                },
                title = "Order History",
                subtitle = "View all past receipts and orders",
                onClick = onOrderHistoryClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            ProfileMenuCard(
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = null,
                        tint = Orange2,
                        modifier = Modifier.size(34.dp)
                    )
                },
                title = "App Settings",
                subtitle = "Notifications, privacy, and theme",
                onClick = onSettingsClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            ProfileMenuCard(
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.HelpOutline,
                        contentDescription = null,
                        tint = Orange2,
                        modifier = Modifier.size(34.dp)
                    )
                },
                title = "Help Center",
                subtitle = "Contact support and FAQs",
                onClick = onHelpCenterClick
            )

            Spacer(modifier = Modifier.height(60.dp))

            LogoutButton(
                onClick = onLogoutClick
            )

            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

// ================= HEADER =================

@Composable
fun HeaderSection() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "✕",
                color = Orange2,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = "XXXX",
                color = DarkBrown,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }

        Image(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

// ================= PROFILE CARD =================

@Composable
fun ProfileCard() {

    Column(
        modifier = Modifier
            .padding(horizontal = 22.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(34.dp))
            .background(White)
            .padding(vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box {

            Image(
                painter = painterResource(id = R.drawable.profile2),
                contentDescription = null,
                modifier = Modifier
                    .size(190.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Orange2),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = "Alex User",
            color = DarkBrown,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Member since October 2022",
            color = SoftBrown,
            fontSize = 18.sp
        )
    }
}

// ================= MENU CARD =================

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
            .clickable {
                onClick()
            }
            .padding(horizontal = 22.dp, vertical = 26.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(88.dp)
                .clip(RoundedCornerShape(26.dp))
                .background(Cream),
            contentAlignment = Alignment.Center
        ) {

            icon()
        }

        Spacer(modifier = Modifier.width(20.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                color = DarkBrown,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                color = SoftBrown,
                fontSize = 16.sp
            )
        }

        Icon(
            imageVector = Icons.Outlined.ArrowForwardIos,
            contentDescription = null,
            tint = SoftBrown,
            modifier = Modifier.size(22.dp)
        )
    }
}

// ================= LOGOUT BUTTON =================

@Composable
fun LogoutButton(
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .padding(horizontal = 22.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(50.dp))
            .background(PrimaryOrange)
            .clickable {
                onClick()
            }
            .padding(vertical = 26.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Outlined.Logout,
            contentDescription = null,
            tint = White,
            modifier = Modifier.size(34.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = "Log Out",
            color = White,
            fontWeight = FontWeight.Medium,
            fontSize = 24.sp
        )
    }
}