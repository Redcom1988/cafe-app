package com.redcom1988.cafej3.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.redcom1988.cafej3.R
import com.redcom1988.cafej3.model.MenuItem
import com.redcom1988.cafej3.theme.*

@Composable
fun FoodCard(
    item: MenuItem,
    onDelete: () -> Unit = {}
) {
    val isSpecial = item.isSpecial
    val isPopular = item.isPopular

    val cardBackground = if (isSpecial) Color(0xFFC64F00) else Color.White
    val textColor      = if (isSpecial) Color(0xFFFFFBFF) else Color.Black
    val descColor      = if (isSpecial) Color(0xFFFFDCC5) else Color.Gray

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .shadow(
                elevation = if (isSpecial) 25.dp else 4.dp,
                spotColor = Color(0x1A000000),
                ambientColor = Color(0x1A000000),
                shape = RoundedCornerShape(32.dp)
            ),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground)
    ) {
        Column {

            // ── Image Box + Price Badge overlay ──────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(Color.LightGray)
            ) {
                if (!isSpecial) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 16.dp, end = 16.dp)
                            .shadow(
                                elevation = 2.dp,
                                spotColor = Color(0x0D000000),
                                ambientColor = Color(0x0D000000),
                                shape = RoundedCornerShape(9999.dp)
                            )
                            .background(
                                color = Color(0xE5FFF8EF),
                                shape = RoundedCornerShape(9999.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Rp ${item.price.toLong()}",
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight(700),
                                color = Color(0xFFC64F00)
                            )
                        )
                    }
                }
            }

            // ── Content ───────────────────────────────────────────────────
            Column(modifier = Modifier.padding(18.dp)) {

                // Chef's Special label
                if (isSpecial) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_chef_star),
                            contentDescription = null,
                            tint = Color(0xFFFFFBFF),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "CHEF'S SPECIAL",
                            style = TextStyle(
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                fontWeight = FontWeight(700),
                                color = Color(0xFFFFFBFF),
                                letterSpacing = 1.2.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Title
                Text(
                    text = item.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Description + Popular label sejajar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = item.description,
                        color = descColor,
                        modifier = Modifier.weight(1f)
                    )

                    if (isPopular) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color(0xFFFFDCC5),
                                    shape = RoundedCornerShape(9999.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Popular",
                                style = TextStyle(
                                    fontSize = 10.sp,
                                    lineHeight = 15.sp,
                                    fontWeight = FontWeight(700),
                                    color = Color(0xFF301400)
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // ── Bottom row: price kiri, Quick Add kanan (special) ──
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (isSpecial) {
                        Text(
                            text = "Rp ${item.price.toLong()}",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFFBFF)
                            )
                        )
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFF8EF)
                            ),
                            shape = RoundedCornerShape(9999.dp)
                        ) {
                            Text(
                                text = "Quick Add",
                                color = Color(0xFF301400),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Button(
                            onClick = {},
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(text = "Add to Cart", fontSize = 16.sp, color = White)
                        }
                    }
                }
            }
        }
    }
}