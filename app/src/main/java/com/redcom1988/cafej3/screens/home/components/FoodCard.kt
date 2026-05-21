package com.redcom1988.cafej3.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.redcom1988.cafej3.theme.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color

@Composable
fun FoodCard() {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(28.dp)
    ) {

        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(Color.LightGray)
            )

            Column(
                modifier = Modifier.padding(18.dp)
            ) {

                Text(
                    text = "Pizza Truffle",
                    fontSize = 22.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Special Italian taste",
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryOrange
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {

                    Text("Add to Cart")
                }
            }
        }
    }
}