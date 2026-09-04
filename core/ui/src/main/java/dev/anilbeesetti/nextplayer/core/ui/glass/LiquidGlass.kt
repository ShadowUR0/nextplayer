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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.isSpecified
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
 * Shared Liquid Glass primitives used by the fork.
 *
 * The visual model follows Kyant0/AndroidLiquidGlass: one recorded [LayerBackdrop] is sampled by
 * all foreground glass surfaces, then blur, saturation and real lens refraction are applied to the
 * pixels behind each control. Keeping this in core/ui lets the player, library and settings migrate
 * to the same glass language without duplicating shader tuning.
 */
enum class LiquidGlassStyle {
    CONTROL,
    PANEL,
    STRONG_PANEL,
}

/** The current screen's recorded background. Null means use the normal non-glass fallback UI. */
val LocalLiquidGlassBackdrop = staticCompositionLocalOf<LayerBackdrop?> { null }

@Composable
fun rememberLiquidGlassBackdrop(): LayerBackdrop = rememberLayerBackdrop()

/** Record this composable subtree so foreground glass can refract it. */
fun Modifier.captureLiquidGlassBackdrop(backdrop: LayerBackdrop): Modifier = layerBackdrop(backdrop)

/**
 * Apply a true backdrop-based liquid glass material to any rounded surface.
 *
 * On devices where Android render effects/runtime shaders are unavailable, Backdrop safely skips
 * the unsupported effect while the translucent surface, highlight and shadows remain as fallback.
 */
@Composable
fun Modifier.liquidGlass(
    backdrop: Backdrop,
    style: LiquidGlassStyle = LiquidGlassStyle.CONTROL,
    shape: Shape = RoundedCornerShape(24.dp),
    surfaceColor: Color = Color.Unspecified,
): Modifier {
    val spec = LiquidGlassSpec.forStyle(style, isSystemInDarkTheme())
    val resolvedSurface = if (surfaceColor.isSpecified) surfaceColor else spec.surfaceColor
    val drawSurface: DrawScope.() -> Unit = { drawRect(resolvedSurface) }

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
                color = Color.Black.copy(alpha = spec.innerShadowAlpha),
            )
        },
        onDrawSurface = drawSurface,
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
                brightness = if (isDark) 0.01f else 0.08f,
                contrast = 1.02f,
                saturation = 1.45f,
                blur = 2.dp,
                refractionHeight = 12.dp,
                refractionAmount = 24.dp,
                chromaticAberration = false,
                highlightWidth = 0.65.dp,
                highlightAlpha = if (isDark) 0.62f else 0.80f,
                shadowRadius = 14.dp,
                shadowAlpha = if (isDark) 0.24f else 0.12f,
                innerShadowRadius = 7.dp,
                innerShadowAlpha = if (isDark) 0.18f else 0.10f,
                surfaceColor = if (isDark) {
                    Color.Black.copy(alpha = 0.24f)
                } else {
                    Color.White.copy(alpha = 0.24f)
                },
            )

            LiquidGlassStyle.PANEL -> LiquidGlassSpec(
                brightness = if (isDark) 0.01f else 0.10f,
                contrast = 1.02f,
                saturation = 1.45f,
                blur = 8.dp,
                refractionHeight = 18.dp,
                refractionAmount = 30.dp,
                chromaticAberration = false,
                highlightWidth = 0.8.dp,
                highlightAlpha = if (isDark) 0.54f else 0.74f,
                shadowRadius = 22.dp,
                shadowAlpha = if (isDark) 0.28f else 0.14f,
                innerShadowRadius = 10.dp,
                innerShadowAlpha = if (isDark) 0.22f else 0.14f,
                surfaceColor = if (isDark) {
                    Color.Black.copy(alpha = 0.28f)
                } else {
                    Color.White.copy(alpha = 0.26f)
                },
            )

            LiquidGlassStyle.STRONG_PANEL -> LiquidGlassSpec(
                brightness = if (isDark) 0.00f else 0.13f,
                contrast = 1.04f,
                saturation = 1.55f,
                blur = 14.dp,
                refractionHeight = 24.dp,
                refractionAmount = 38.dp,
                chromaticAberration = false,
                highlightWidth = 1.dp,
                highlightAlpha = if (isDark) 0.58f else 0.82f,
                shadowRadius = 30.dp,
                shadowAlpha = if (isDark) 0.32f else 0.16f,
                innerShadowRadius = 12.dp,
                innerShadowAlpha = if (isDark) 0.26f else 0.16f,
                surfaceColor = if (isDark) {
                    Color.Black.copy(alpha = 0.34f)
                } else {
                    Color.White.copy(alpha = 0.30f)
                },
            )
        }
    }
}
