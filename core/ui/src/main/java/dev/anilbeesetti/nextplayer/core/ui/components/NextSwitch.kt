package dev.anilbeesetti.nextplayer.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.anilbeesetti.nextplayer.core.ui.designsystem.NextIcons
import dev.anilbeesetti.nextplayer.core.ui.glass.LiquidGlassStyle
import dev.anilbeesetti.nextplayer.core.ui.glass.LocalLiquidGlassBackdrop
import dev.anilbeesetti.nextplayer.core.ui.glass.liquidGlass

@Composable
fun NextSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    checkedIcon: ImageVector = NextIcons.Check,
) {
    val glassBackdrop = LocalLiquidGlassBackdrop.current

    if (glassBackdrop != null) {
        val layoutDirection = LocalLayoutDirection.current
        val haptic = LocalHapticFeedback.current
        val travel by animateDpAsState(
            targetValue = if (checked) 22.dp else 0.dp,
            animationSpec = spring(dampingRatio = 0.76f, stiffness = 420f),
            label = "liquidSwitchTravel",
        )
        val thumbScale by animateFloatAsState(
            targetValue = if (checked) 1.06f else 1f,
            animationSpec = spring(dampingRatio = 0.74f, stiffness = 380f),
            label = "liquidSwitchThumbScale",
        )
        val trackColor by animateColorAsState(
            targetValue = when {
                !enabled -> Color.White.copy(alpha = 0.08f)
                checked -> Color(0xFF30D158).copy(alpha = 0.42f)
                else -> Color.White.copy(alpha = 0.10f)
            },
            label = "liquidSwitchTrack",
        )

        Box(
            modifier = modifier
                .size(width = 52.dp, height = 30.dp)
                .liquidGlass(
                    backdrop = glassBackdrop,
                    style = LiquidGlassStyle.CONTROL,
                    shape = CircleShape,
                    surfaceColor = trackColor,
                )
                .then(
                    if (onCheckedChange != null) {
                        Modifier.toggleable(
                            value = checked,
                            enabled = enabled,
                            role = Role.Switch,
                            onValueChange = { newValue ->
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onCheckedChange(newValue)
                            },
                        )
                    } else {
                        Modifier
                    },
                ),
            contentAlignment = Alignment.CenterStart,
        ) {
            val signedTravel = if (layoutDirection == LayoutDirection.Ltr) travel else -travel
            Box(
                modifier = Modifier
                    .offset(x = signedTravel)
                    .size(30.dp)
                    .graphicsLayer {
                        scaleX = thumbScale
                        scaleY = thumbScale
                    }
                    .liquidGlass(
                        backdrop = glassBackdrop,
                        style = LiquidGlassStyle.CONTROL,
                        shape = CircleShape,
                        surfaceColor = Color.White.copy(alpha = if (enabled) 0.22f else 0.08f),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                if (checked) {
                    Icon(
                        imageVector = checkedIcon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp),
                    )
                }
            }
        }
        return
    }

    val thumbContent: (@Composable () -> Unit)? = if (checked) {
        {
            Icon(
                imageVector = checkedIcon,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize),
            )
        }
    } else {
        null
    }

    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        thumbContent = thumbContent,
    )
}
