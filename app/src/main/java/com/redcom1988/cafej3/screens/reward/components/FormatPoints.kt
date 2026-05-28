package com.redcom1988.cafej3.screens.reward.components

import java.text.NumberFormat
import java.util.Locale

fun Int.formatPoints(): String = NumberFormat.getNumberInstance(Locale.US).format(this)