package com.haman.jetsnackclone.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector

@Stable
class Filter(
    val name: String,
    enabled: Boolean = false,
    val icon: ImageVector? = null
) {
    val enabled = mutableStateOf(enabled)
}

// Snack filter
val filters = listOf(
    Filter(name = "Organic"),
    Filter(name = "Gluten-Free"),
    Filter(name = "Diary-Free"),
    Filter(name = "Sweet"),
    Filter(name = "Savory")
)

// 가격 Filter
val priceFilters = listOf(
    Filter(name = "$"),
    Filter(name = "$$"),
    Filter(name = "$$$"),
    Filter(name = "$$$$")
)

// 정렬 Filter
val sortFilters = listOf(
    Filter(name = "Android's Favorite(Default)", icon = Icons.Filled.Android),
    Filter(name = "Rating", icon = Icons.Filled.Star),
    Filter(name = "Alphabetical", icon = Icons.Filled.SortByAlpha)
)

// Category Filter
val categoryFilters = listOf(
    Filter(name = "Chips & Crackers"),
    Filter(name = "Fruit snacks"),
    Filter(name = "Desserts"),
    Filter(name = "Nuts")
)

// 라이프 스타일 별 Filter
val lifeStyleFilters = listOf(
    Filter(name = "Organic"),
    Filter(name = "Gluten-Free"),
    Filter(name = "Diary-Free"),
    Filter(name = "Sweet"),
    Filter(name = "Savory")
)

val sortDefault = sortFilters[0].name