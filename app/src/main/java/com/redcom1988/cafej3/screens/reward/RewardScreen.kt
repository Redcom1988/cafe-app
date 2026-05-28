package com.redcom1988.cafej3.screens.reward

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import kotlinx.coroutines.launch
import com.redcom1988.cafej3.R
import com.redcom1988.cafej3.components.Header
import com.redcom1988.cafej3.data.DummyData
import com.redcom1988.cafej3.model.RewardItem
import com.redcom1988.cafej3.model.TopUser
import com.redcom1988.cafej3.screens.reward.components.*
import com.redcom1988.cafej3.theme.Background
import com.redcom1988.cafej3.theme.SoftBrown

object RewardScreen : Tab {

    private fun readResolve(): Any = RewardScreen

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Outlined.CardGiftcard)
            return remember { TabOptions(index = 2u, title = "REWARDS", icon = icon) }
        }

    @Composable
    override fun Content() {
        var currentPoints by remember { mutableStateOf(1250) }
        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        val rewards = remember { DummyData.rewards }
        val users = remember { DummyData.users }

        val handleRedeem: (RewardItem) -> Unit = { reward ->
            if (currentPoints >= reward.cost) {
                currentPoints -= reward.cost
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Success! You redeemed ${reward.title}")
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Background)
            ) {
                item {
                    Header()

                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Spacer(modifier = Modifier.height(16.dp))
                        RewardBalanceCard(currentPoints = currentPoints)
                        Spacer(modifier = Modifier.height(20.dp))
                        SectionTitle(title = "Available Rewards", action = "See all")
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp)) {
                        items(rewards) { reward ->
                            RewardCard(
                                item = reward,
                                isAffordable = currentPoints >= reward.cost,
                                onRedeem = handleRedeem
                            )
                        }
                    }
                }

                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Top 4 Users",
                            color = SoftBrown,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        LeaderboardCard(users)
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 80.dp)
            )
        }
    }
}