package com.redcom1988.cafej3.data

import androidx.compose.runtime.remember
import com.redcom1988.cafej3.model.MenuItem
import com.redcom1988.cafej3.R
import com.redcom1988.cafej3.model.RewardItem
import com.redcom1988.cafej3.model.TopUser

object DummyData {

    val menuItems = listOf(
        MenuItem(
            id = 1,
            title = "Pizza Truffle",
            description = "Special Italian taste",
            price = 85000.0,
            category = "Main Course",
            image = R.drawable.food1
        ),
        MenuItem(
            id = 2,
            title = "Bruschetta",
            description = "Toasted bread with tomato",
            price = 45000.0,
            category = "Appetizers",
            image = R.drawable.food2
        ),
        MenuItem(
            id = 3,
            title = "Pasta Carbonara",
            description = "Creamy Roman classic",
            price = 75000.0,
            category = "Main Course",
            image = R.drawable.food3,
            isPopular = true
        ),
        MenuItem(
            id = 4,
            title = "Calamari Fritti",
            description = "Crispy fried squid",
            price = 55000.0,
            category = "Appetizers",
            image = R.drawable.food4
        ),
        MenuItem(
            id = 5,
            title = "Tiramisu",
            description = "Classic Italian dessert",
            price = 40000.0,
            category = "Desserts",
            image = R.drawable.food4,
            isSpecial = true
        ),
        MenuItem(
            id = 6,
            title = "Risotto Funghi",
            description = "Wild mushroom risotto",
            price = 80000.0,
            category = "Main Course",
            image = R.drawable.food5
        ),
    )

    val categories = listOf("Menu", "Appetizers", "Main Course", "Desserts")
    val users =
        listOf(
            TopUser(1, R.drawable.profile, "Alex Johnson",  "Gold Member",   5200),
            TopUser(2, R.drawable.profile, "Sarah Jenkins", "Silver Member", 4850),
            TopUser(3, R.drawable.profile, "Michael Chen",  "Bronze Member", 4100),
            TopUser(4, R.drawable.profile, "Emma Watson",   "Active Member", 3900)
        )
    val rewards =
        listOf(
            RewardItem(1, R.drawable.reward1, 500,  "Free Espresso"),
            RewardItem(2, R.drawable.reward2, 800,  "10% Off Pastry"),
            RewardItem(3, R.drawable.reward3, 1200, "Free Dessert")
        )
}