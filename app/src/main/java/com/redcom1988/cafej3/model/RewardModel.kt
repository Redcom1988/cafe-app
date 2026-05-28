package com.redcom1988.cafej3.model

data class RewardItem(
    val id: Int,
    val image: Int,
    val cost: Int,
    val title: String
)

data class TopUser(
    val rank: Int,
    val image: Int,
    val name: String,
    val status: String,
    val points: Int
)