package dev.anilbeesetti.nextplayer.core.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.anilbeesetti.nextplayer.core.ui.glass.LiquidGlassStyle
import dev.anilbeesetti.nextplayer.core.ui.glass.LocalLiquidGlassBackdrop
import dev.anilbeesetti.nextplayer.core.ui.glass.liquidGlass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NextTopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ),
) {
    val backdrop = LocalLiquidGlassBackdrop.current
    val shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
    val resolvedModifier = if (backdrop != null) {
        modifier.liquidGlass(
            backdrop = backdrop,
            style = LiquidGlassStyle.PANEL,
            shape = shape,
            surfaceColor = Color.Unspecified,
        )
    } else {
        modifier
    }
    val resolvedColors = if (backdrop != null) {
        TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    } else {
        colors
    }

    TopAppBar(
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
        colors = resolvedColors,
        modifier = resolvedModifier,
        contentPadding = PaddingValues(horizontal = 8.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NextTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    fontWeight: FontWeight? = null,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ),
) {
    NextTopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = fontWeight,
            )
        },
        navigationIcon = navigationIcon,
        actions = actions,
        colors = colors,
        modifier = modifier,
    )
}
