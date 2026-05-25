package com.redcom1988.cafej3.screens.reward

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.redcom1988.cafej3.components.*
import com.redcom1988.cafej3.theme.*

// ================= DATA =================

data class RewardItem(
    val image: Int,
    val points: String,
    val title: String
)

data class TopUser(
    val rank: Int,
    val image: Int,
    val name: String,
    val status: String,
    val points: String
)

// ================= SCREEN =================

@Composable
fun RewardScreen() {

    val rewards = listOf(
        RewardItem(R.drawable.reward1, "500 Points", "Free Espresso"),
        RewardItem(R.drawable.reward2, "800 Points", "10% Off Pastry"),
        RewardItem(R.drawable.reward3, "1200 Points", "Free Dessert")
    )

    val users = listOf(
        TopUser(1, R.drawable.profile, "Alex Johnson", "Gold Member", "5,200"),
        TopUser(2, R.drawable.profile, "Sarah Jenkins", "Silver Member", "4,850"),
        TopUser(3, R.drawable.profile, "Michael Chen", "Bronze Member", "4,100"),
        TopUser(4, R.drawable.profile, "Emma Watson", "Active Member", "3,900")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 20.dp)
    ) {

        item {

            Spacer(modifier = Modifier.height(20.dp))

            HeaderSection()

            Spacer(modifier = Modifier.height(36.dp))

            RewardBalanceCard()

            Spacer(modifier = Modifier.height(36.dp))

            SectionTitle(
                title = "Available Rewards",
                action = "See all"
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        item {

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(rewards) {
                    RewardCard(it)
                }
            }
        }

        item {

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = "Top 4 Users",
                color = SoftBrown,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            LeaderboardCard(users)

            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

// ================= HEADER =================

@Composable
fun HeaderSection() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "✕",
                color = Orange3,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.width(12.dp))

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
                .size(54.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

// ================= BALANCE CARD =================

@Composable
fun RewardBalanceCard() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        Orange2,
                        PrimaryOrange
                    )
                )
            )
            .padding(24.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {

                Text(
                    text = "Current Balance",
                    color = White,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.Bottom
                ) {

                    Text(
                        text = "1,250",
                        color = White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 52.sp
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Points",
                        color = White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = White,
                modifier = Modifier.size(42.dp)
            )
        }

        Spacer(modifier = Modifier.height(26.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "Gold Member Status",
                color = White,
                fontSize = 18.sp
            )

            Text(
                text = "250 to next reward",
                color = White,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        LinearProgressIndicator(
            progress = { 0.7f },
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(CircleShape),
            color = White,
            trackColor = Color(0xFF9A3A00)
        )
    }
}

// ================= SECTION TITLE =================

@Composable
fun SectionTitle(
    title: String,
    action: String
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = title,
            color = SoftBrown,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp
        )

        Text(
            text = action,
            color = Orange2,
            fontSize = 18.sp
        )
    }
}

// ================= REWARD CARD =================

@Composable
fun RewardCard(
    item: RewardItem
) {

    Column(
        modifier = Modifier
            .width(300.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(White)
            .padding(18.dp)
    ) {

        Image(
            painter = painterResource(id = item.image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .clip(RoundedCornerShape(22.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = item.points,
            color = Orange2,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.title,
            color = DarkBrown,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Cream)
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "Redeem",
                color = SoftBrown,
                fontSize = 20.sp
            )
        }
    }
}

// ================= LEADERBOARD =================

@Composable
fun LeaderboardCard(
    users: List<TopUser>
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(White)
            .padding(vertical = 8.dp)
    ) {

        users.forEachIndexed { index, user ->

            LeaderboardItem(user)

            if (index != users.lastIndex) {

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Cream)
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

// ================= LEADERBOARD ITEM =================

@Composable
fun LeaderboardItem(
    user: TopUser
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = user.rank.toString(),
            color = if (user.rank == 1) Orange2 else Color.Gray,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.width(18.dp))

        Image(
            painter = painterResource(id = user.image),
            contentDescription = null,
            modifier = Modifier
                .size(62.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(18.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = user.name,
                color = DarkBrown,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = user.status,
                color = Color.Gray,
                fontSize = 16.sp
            )
        }

        Row(
            verticalAlignment = Alignment.Bottom
        ) {

            Text(
                text = user.points,
                color = Orange2,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "pts",
                color = Orange2,
                fontSize = 16.sp
            )
        }
    }
}