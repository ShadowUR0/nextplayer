package dev.anilbeesetti.nextplayer.core.ui.glass

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.matchParentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * App-level backdrop host.
 *
 * The decorative base layer is recorded separately from the interactive content. Glass controls can
 * therefore sample a stable backdrop without recording themselves and causing recursive rendering.
 * Feature screens may still provide a more specific backdrop (the video player and top-level
 * navigation do this) by overriding [LocalLiquidGlassBackdrop].
 */
@Composable
fun LiquidGlassScaffold(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val backdrop = rememberLiquidGlassBackdrop()
    val colors = MaterialTheme.colorScheme
    val ambientBrush = Brush.linearGradient(
        colors = listOf(
            colors.surfaceContainerLowest,
            colors.surfaceContainer,
            colors.primary.copy(alpha = 0.10f),
            colors.tertiary.copy(alpha = 0.08f),
            colors.surfaceContainerHigh,
        ),
    )

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .captureLiquidGlassBackdrop(backdrop)
                .background(ambientBrush),
        )
        CompositionLocalProvider(LocalLiquidGlassBackdrop provides backdrop) {
            Box(
                modifier = Modifier.matchParentSize(),
                content = content,
            )
        }
    }
}
