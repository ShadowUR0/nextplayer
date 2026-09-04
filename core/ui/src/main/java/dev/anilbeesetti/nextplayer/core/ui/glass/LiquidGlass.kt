package dev.anilbeesetti.nextplayer.core.ui.glass

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.colorControls
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.highlight.Highlight
import com.kyant.backdrop.shadow.InnerShadow
import com.kyant.backdrop.shadow.Shadow

/**
 * Shared Liquid Glass primitives for Next Player.
 *
 * Rendering is powered by Kyant0/AndroidLiquidGlass (Backdrop). Components consume a single
 * [LayerBackdrop] captured from the content behind them so refraction stays spatially correct and
 * the whole application can be tuned from one place during the migration.
 */
enum class LiquidGlassStyle {
    CONTROL,
    PANEL,
    STRONG_PANEL,
}

val LocalLiquidGlassBackdrop = staticCompositionLocalOf<LayerBackdrop?> { null }

@Composable
fun rememberLiquidGlassBackdrop(): LayerBackdrop = rememberLayerBackdrop()

fun Modifier.captureLiquidGlassBackdrop(backdrop: LayerBackdrop): Modifier = layerBackdrop(backdrop)

@Composable
fun Modifier.liquidGlass(
    backdrop: Backdrop,
    style: LiquidGlassStyle = LiquidGlassStyle.CONTROL,
    shape: Shape = RoundedCornerShape(24.dp),
    surfaceColor: Color = Color.Unspecified,
): Modifier {
    val isDark = isSystemInDarkTheme()
    val spec = LiquidGlassSpec.forStyle(style, isDark)
    val resolvedSurface = if (surfaceColor == Color.Unspecified) spec.surfaceColor else surfaceColor

    return drawBackdrop(
        backdrop = backdrop,
        shape = { shape },
        effects = {
            colorControls(
                brightness = spec.brightness,
                contrast = spec.contrast,
                saturation = spec.saturation,
            )
            blur(spec.blur.toPx())
            lens(
                refractionHeight = spec.refractionHeight.toPx(),
                refractionAmount = spec.refractionAmount.toPx(),
                depthEffect = true,
                chromaticAberration = spec.chromaticAberration,
            )
        },
        highlight = {
            Highlight.Default.copy(
                width = spec.highlightWidth,
                alpha = spec.highlightAlpha,
            )
        },
        shadow = {
            Shadow(
                radius = spec.shadowRadius,
                color = Color.Black.copy(alpha = spec.shadowAlpha),
            )
        },
        innerShadow = {
            InnerShadow(
                radius = spec.innerShadowRadius,
                alpha = spec.innerShadowAlpha,
            )
        },
        onDrawSurface = resolvedSurface.takeIf { it != Color.Unspecified }?.let { color ->
            { drawRect(color) }
        },
    )
}

@Composable
fun LiquidGlassSurface(
    backdrop: Backdrop,
    modifier: Modifier = Modifier,
    style: LiquidGlassStyle = LiquidGlassStyle.PANEL,
    shape: Shape = RoundedCornerShape(28.dp),
    surfaceColor: Color = Color.Unspecified,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier.liquidGlass(
            backdrop = backdrop,
            style = style,
            shape = shape,
            surfaceColor = surfaceColor,
        ),
        content = content,
    )
}

private data class LiquidGlassSpec(
    val brightness: Float,
    val contrast: Float,
    val saturation: Float,
    val blur: Dp,
    val refractionHeight: Dp,
    val refractionAmount: Dp,
    val chromaticAberration: Boolean,
    val highlightWidth: Dp,
    val highlightAlpha: Float,
    val shadowRadius: Dp,
    val shadowAlpha: Float,
    val innerShadowRadius: Dp,
    val innerShadowAlpha: Float,
    val surfaceColor: Color,
) {
    companion object {
        fun forStyle(style: LiquidGlassStyle, isDark: Boolean): LiquidGlassSpec = when (style) {
            LiquidGlassStyle.CONTROL -> LiquidGlassSpec(
                brightness = if (isDark) 0.02f else 0.10f,
                contrast = 1.02f,
                saturation = 1.35f,
                blur = 3.dp,
                refractionHeight = 10.dp,
                refractionAmount = 18.dp,
                chromaticAberration = false,
                highlightWidth = 0.75.dp,
                highlightAlpha = if (isDark) 0.58f else 0.76f,
                shadowRadius = 16.dp,
                shadowAlpha = if (isDark) 0.22f else 0.12f,
                innerShadowRadius = 8.dp,
                innerShadowAlpha = if (isDark) 0.24f else 0.16f,
                surfaceColor = if (isDark) {
                    Color.Black.copy(alpha = 0.20f)
                } else {
                    Color.White.copy(alpha = 0.18f)
                },
            )

            LiquidGlassStyle.PANEL -> LiquidGlassSpec(
                brightness = if (isDark) 0.01f else 0.12f,
                contrast = 1.02f,
                saturation = 1.45f,
                blur = 8.dp,
                refractionHeight = 18.dp,
                refractionAmount = 28.dp,
                chromaticAberration = false,
                highlightWidth = 0.8.dp,
                highlightAlpha = if (isDark) 0.52f else 0.72f,
                shadowRadius = 24.dp,
                shadowAlpha = if (isDark) 0.28f else 0.14f,
                innerShadowRadius = 10.dp,
                innerShadowAlpha = if (isDark) 0.26f else 0.18f,
                surfaceColor = if (isDark) {
                    Color.Black.copy(alpha = 0.24f)
                } else {
                    Color.White.copy(alpha = 0.22f)
                },
            )

            LiquidGlassStyle.STRONG_PANEL -> LiquidGlassSpec(
                brightness = if (isDark) 0.00f else 0.15f,
                contrast = 1.04f,
                saturation = 1.55f,
                blur = 14.dp,
                refractionHeight = 24.dp,
                refractionAmount = 38.dp,
                chromaticAberration = false,
                highlightWidth = 1.dp,
                highlightAlpha = if (isDark) 0.56f else 0.80f,
                shadowRadius = 30.dp,
                shadowAlpha = if (isDark) 0.32f else 0.16f,
                innerShadowRadius = 12.dp,
                innerShadowAlpha = if (isDark) 0.28f else 0.20f,
                surfaceColor = if (isDark) {
                    Color.Black.copy(alpha = 0.30f)
                } else {
                    Color.White.copy(alpha = 0.28f)
                },
            )
        }
    }
}
