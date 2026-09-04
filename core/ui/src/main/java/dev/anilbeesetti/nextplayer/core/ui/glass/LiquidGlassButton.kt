package dev.anilbeesetti.nextplayer.core.ui.glass

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Reusable interactive liquid-glass button inspired by Tianyin Wallpaper's control language.
 *
 * It keeps native Compose semantics while adding subtle elastic deformation. The actual
 * blur/refraction is provided by AndroidLiquidGlass Backdrop through [liquidGlass].
 */
@Composable
fun LiquidGlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: LiquidGlassStyle = LiquidGlassStyle.CONTROL,
    shape: Shape = RoundedCornerShape(24.dp),
    surfaceColor: Color = Color.Unspecified,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    horizontalPadding: Dp = 16.dp,
    content: @Composable RowScope.() -> Unit,
) {
    val backdrop = LocalLiquidGlassBackdrop.current
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scaleX by animateFloatAsState(
        targetValue = if (pressed && enabled) 1.035f else 1f,
        animationSpec = spring(dampingRatio = 0.72f, stiffness = 420f),
        label = "liquidButtonScaleX",
    )
    val scaleY by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.965f else 1f,
        animationSpec = spring(dampingRatio = 0.72f, stiffness = 420f),
        label = "liquidButtonScaleY",
    )

    val baseModifier = modifier
        .graphicsLayer {
            this.scaleX = scaleX
            this.scaleY = scaleY
        }
        .then(
            if (backdrop != null) {
                Modifier.liquidGlass(
                    backdrop = backdrop,
                    style = style,
                    shape = shape,
                    surfaceColor = surfaceColor,
                )
            } else {
                Modifier
            },
        )
        .clickable(
            interactionSource = interactionSource,
            indication = if (backdrop == null) LocalIndication.current else null,
            enabled = enabled,
            role = Role.Button,
            onClick = onClick,
        )
        .defaultMinSize(minHeight = 48.dp)
        .padding(horizontal = horizontalPadding)

    CompositionLocalProvider(
        LocalContentColor provides contentColor.copy(alpha = if (enabled) 1f else 0.48f),
    ) {
        Row(
            modifier = baseModifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}
