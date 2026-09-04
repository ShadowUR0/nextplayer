package dev.anilbeesetti.nextplayer.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.anilbeesetti.nextplayer.core.ui.designsystem.NextIcons
import dev.anilbeesetti.nextplayer.core.ui.glass.LiquidGlassStyle
import dev.anilbeesetti.nextplayer.core.ui.glass.LocalLiquidGlassBackdrop
import dev.anilbeesetti.nextplayer.core.ui.glass.liquidGlass

@Composable
fun LiquidGlassRadio(
    selected: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val backdrop = LocalLiquidGlassBackdrop.current
    val outlineColor = when {
        !enabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.28f)
        selected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.92f)
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.54f)
    }
    val baseModifier = modifier.size(24.dp)
    val glassModifier = if (backdrop != null) {
        baseModifier.liquidGlass(
            backdrop = backdrop,
            style = LiquidGlassStyle.CONTROL,
            shape = CircleShape,
            surfaceColor = if (selected) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
            } else {
                Color.White.copy(alpha = 0.06f)
            },
        )
    } else {
        baseModifier.background(Color.Transparent, CircleShape)
    }

    Box(
        modifier = glassModifier.border(1.5.dp, outlineColor, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = if (enabled) 1f else 0.42f),
                        CircleShape,
                    ),
            )
        }
    }
}

@Composable
fun LiquidGlassCheckbox(
    checked: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    checkedIcon: ImageVector = NextIcons.Check,
) {
    val backdrop = LocalLiquidGlassBackdrop.current
    val shape = RoundedCornerShape(7.dp)
    val outlineColor = when {
        !enabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.28f)
        checked -> MaterialTheme.colorScheme.primary.copy(alpha = 0.92f)
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.54f)
    }
    val baseModifier = modifier.size(24.dp)
    val glassModifier = if (backdrop != null) {
        baseModifier.liquidGlass(
            backdrop = backdrop,
            style = LiquidGlassStyle.CONTROL,
            shape = shape,
            surfaceColor = if (checked) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)
            } else {
                Color.White.copy(alpha = 0.06f)
            },
        )
    } else {
        baseModifier.background(
            if (checked) MaterialTheme.colorScheme.primary.copy(alpha = 0.16f) else Color.Transparent,
            shape,
        )
    }

    Box(
        modifier = glassModifier.border(1.5.dp, outlineColor, shape),
        contentAlignment = Alignment.Center,
    ) {
        if (checked) {
            Icon(
                imageVector = checkedIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(15.dp),
            )
        }
    }
}
