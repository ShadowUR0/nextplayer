package dev.anilbeesetti.nextplayer.core.ui.glass

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
 * The track and thumb both sample the current backdrop. The thumb stretches under pressure to keep
 * the tactile, elastic interaction language used by Tianyin while Material3 still provides native
 * slider semantics, keyboard support and accessibility.
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
        Color.White.copy(alpha = if (enabled) 0.12f else 0.06f)
    } else {
        inactiveColor
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val thumbScaleX by animateFloatAsState(
        targetValue = if (isPressed && enabled) 1.34f else 1f,
        animationSpec = spring(dampingRatio = 0.68f, stiffness = 360f),
        label = "liquidSliderThumbScaleX",
    )
    val thumbScaleY by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.90f else 1f,
        animationSpec = spring(dampingRatio = 0.72f, stiffness = 400f),
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
            val baseTrackModifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
            val trackModifier = if (backdrop != null) {
                baseTrackModifier.liquidGlass(
                    backdrop = backdrop,
                    style = LiquidGlassStyle.CONTROL,
                    shape = CircleShape,
                    surfaceColor = resolvedInactiveColor,
                )
            } else {
                baseTrackModifier.background(resolvedInactiveColor, CircleShape)
            }

            Box(modifier = trackModifier.clip(CircleShape)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction)
                        .fillMaxHeight()
                        .background(
                            resolvedActiveColor.copy(alpha = if (enabled) 0.92f else 0.32f),
                            CircleShape,
                        ),
                )
            }
        },
        thumb = {
            val baseModifier = Modifier
                .size(width = 36.dp, height = 22.dp)
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
                                isPressed -> 0.26f
                                else -> 0.14f
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
