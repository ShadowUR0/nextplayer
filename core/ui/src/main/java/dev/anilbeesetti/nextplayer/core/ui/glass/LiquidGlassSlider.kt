package dev.anilbeesetti.nextplayer.core.ui.glass

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

/**
 * Liquid-glass slider shared by the player and settings UI.
 *
 * The thumb is a real Backdrop lens rather than a translucent imitation. The small elastic press
 * animation mirrors the interaction language used by AndroidLiquidGlass/Tianyin controls while
 * keeping Material3 Slider semantics, focus and keyboard/TV behavior.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiquidGlassSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    activeColor: Color = Color.Unspecified,
    inactiveColor: Color = Color.Unspecified,
) {
    val backdrop = LocalLiquidGlassBackdrop.current
    val resolvedActiveColor = if (activeColor == Color.Unspecified) {
        MaterialTheme.colorScheme.primary
    } else {
        activeColor
    }
    val resolvedInactiveColor = if (inactiveColor == Color.Unspecified) {
        Color.White.copy(alpha = if (enabled) 0.24f else 0.10f)
    } else {
        inactiveColor
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val thumbScaleX by animateFloatAsState(
        targetValue = if (isPressed && enabled) 1.28f else 1f,
        label = "liquidSliderThumbScaleX",
    )
    val thumbScaleY by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.92f else 1f,
        label = "liquidSliderThumbScaleY",
    )

    Slider(
        value = value.coerceIn(valueRange),
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        valueRange = valueRange,
        steps = steps,
        onValueChangeFinished = onValueChangeFinished,
        interactionSource = interactionSource,
        track = { sliderState ->
            val min = sliderState.valueRange.start
            val max = sliderState.valueRange.endInclusive
            val range = (max - min).takeIf { it > 0f } ?: 1f
            val fraction = ((sliderState.value - min) / range).coerceIn(0f, 1f)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(resolvedInactiveColor),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction)
                        .fillMaxHeight()
                        .background(resolvedActiveColor.copy(alpha = if (enabled) 1f else 0.38f)),
                )
            }
        },
        thumb = {
            val baseModifier = Modifier
                .size(width = 34.dp, height = 22.dp)
                .graphicsLayer {
                    scaleX = thumbScaleX
                    scaleY = thumbScaleY
                }

            Box(
                modifier = if (backdrop != null) {
                    baseModifier.liquidGlass(
                        backdrop = backdrop,
                        style = LiquidGlassStyle.CONTROL,
                        shape = CircleShape,
                        surfaceColor = Color.White.copy(
                            alpha = when {
                                !enabled -> 0.08f
                                isPressed -> 0.30f
                                else -> 0.16f
                            },
                        ),
                    )
                } else {
                    baseModifier.background(
                        Color.White.copy(alpha = if (enabled) 1f else 0.42f),
                        CircleShape,
                    )
                },
            )
        },
    )
}
