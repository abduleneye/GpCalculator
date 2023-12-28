package com.engpacalculator.gpcalculator.features.five_grading_system_sgpa_features.presentation.ads_components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize


fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition()
    val startoffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        )
    )

    background(

        brush = Brush.linearGradient(
            colors = listOf(
                Color(0x4BFCF0F0),
                Color(0xBF0F0E0E),
                Color(0xFFA3A8AF),

                ),
            start = Offset(startoffsetX, 0f),
            end = Offset(startoffsetX + size.width.toFloat(), size.height.toFloat())


        )

    ).onGloballyPositioned {
        size = it.size
    }
}
