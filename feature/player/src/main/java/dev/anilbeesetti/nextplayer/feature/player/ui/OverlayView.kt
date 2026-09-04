package dev.anilbeesetti.nextplayer.feature.player.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.anilbeesetti.nextplayer.core.common.extensions.isTelevision
import dev.anilbeesetti.nextplayer.core.ui.components.requestFocusUntilLanded
import dev.anilbeesetti.nextplayer.core.ui.glass.LiquidGlassStyle
import dev.anilbeesetti.nextplayer.core.ui.glass.LiquidGlassSurface
import dev.anilbeesetti.nextplayer.core.ui.glass.LocalLiquidGlassBackdrop
import dev.anilbeesetti.nextplayer.core.ui.theme.NextPlayerTheme

@Composable
fun BoxScope.OverlayView(
    modifier: Modifier = Modifier,
    show: Boolean,
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val isTv = remember { context.isTelevision }
    val layoutDirection = LocalLayoutDirection.current
    val endPadding = WindowInsets.safeDrawing
        .asPaddingValues()
        .calculateEndPadding(layoutDirection)
    val glassBackdrop = LocalLiquidGlassBackdrop.current
    val isPortrait = configuration.isPortrait

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(show) {
        if (show && isTv) {
            focusRequester.requestFocusUntilLanded(attempts = 5)
        }
    }

    AnimatedVisibility(
        modifier = Modifier.align(
            if (isPortrait) {
                Alignment.BottomCenter
            } else {
                Alignment.CenterEnd
            },
        ),
        visible = show,
        enter = if (isPortrait) slideInVertically { it } else slideInHorizontally { it },
        exit = if (isPortrait) slideOutVertically { it } else slideOutHorizontally { it },
    ) {
        val panelModifier = modifier.then(
            if (isPortrait) {
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.45f)
            } else {
                Modifier
                    .fillMaxWidth(0.45f)
                    .fillMaxHeight()
            },
        )
        val panelShape = if (isPortrait) {
            RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
        } else {
            RoundedCornerShape(topStart = 30.dp, bottomStart = 30.dp)
        }

        @Composable
        fun PanelContent() {
            Column(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .focusGroup()
                    .padding(top = 24.dp)
                    .padding(end = endPadding),
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = if (glassBackdrop != null) Color.White else MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.size(8.dp))
                content()
            }
        }

        if (glassBackdrop != null) {
            LiquidGlassSurface(
                backdrop = glassBackdrop,
                modifier = panelModifier,
                style = LiquidGlassStyle.STRONG_PANEL,
                shape = panelShape,
                surfaceColor = Color.Black.copy(alpha = 0.28f),
            ) {
                PanelContent()
            }
        } else {
            Surface(
                shape = panelShape,
                modifier = panelModifier,
            ) {
                PanelContent()
            }
        }
    }
}

@Preview
@Composable
private fun PreviewOverlayView() {
    NextPlayerTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            OverlayView(modifier = Modifier.align(Alignment.BottomCenter), title = "Selector view", show = true) {
                Text("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Lorem ipsum")
            }
        }
    }
}
