package dev.anilbeesetti.nextplayer.core.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dev.anilbeesetti.nextplayer.core.ui.glass.LiquidGlassStyle
import dev.anilbeesetti.nextplayer.core.ui.glass.LocalLiquidGlassBackdrop
import dev.anilbeesetti.nextplayer.core.ui.glass.liquidGlass

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NextSegmentedListItem(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    enabled: Boolean = true,
    isFirstItem: Boolean = false,
    isLastItem: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    colors: ListItemColors = ListItemDefaults.segmentedColors(),
    shapes: ListItemShapes = ListItemDefaults.shapes(),
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    overlineContent: @Composable (() -> Unit)? = null,
    supportingContent: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
    onClick: () -> Unit = {},
    onLongClick: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    val overrideShape = MaterialTheme.shapes.large
    val backdrop = LocalLiquidGlassBackdrop.current

    if (backdrop != null) {
        val glassShape = RoundedCornerShape(
            topStart = if (isFirstItem) 24.dp else 12.dp,
            topEnd = if (isFirstItem) 24.dp else 12.dp,
            bottomStart = if (isLastItem) 24.dp else 12.dp,
            bottomEnd = if (isLastItem) 24.dp else 12.dp,
        )
        val focusInteractionSource = interactionSource ?: remember { MutableInteractionSource() }
        val isFocused by focusInteractionSource.collectIsFocusedAsState()
        val focusScale by animateFloatAsState(
            targetValue = if (isFocused) 1.012f else 1f,
            animationSpec = spring(dampingRatio = 0.78f, stiffness = 420f),
            label = "liquidListFocusScale",
        )
        val contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = if (enabled) 1f else 0.48f)

        CompositionLocalProvider(LocalContentColor provides contentColor) {
            Row(
                modifier = modifier
                    .zIndex(if (isFocused) 1f else 0f)
                    .graphicsLayer {
                        scaleX = focusScale
                        scaleY = focusScale
                    }
                    .liquidGlass(
                        backdrop = backdrop,
                        style = LiquidGlassStyle.PANEL,
                        shape = glassShape,
                        surfaceColor = if (selected) {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)
                        } else {
                            Color.Unspecified
                        },
                    )
                    .then(
                        if (isFocused) {
                            Modifier.border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.84f),
                                shape = glassShape,
                            )
                        } else {
                            Modifier
                        },
                    )
                    .combinedClickable(
                        enabled = enabled,
                        onLongClick = onLongClick,
                        onClick = onClick,
                    )
                    .padding(contentPadding),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                leadingContent?.let {
                    Box(contentAlignment = Alignment.Center) {
                        it()
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                }

                Column(modifier = Modifier.weight(1f)) {
                    overlineContent?.let {
                        ProvideTextStyle(MaterialTheme.typography.labelSmall) {
                            it()
                        }
                    }
                    ProvideTextStyle(MaterialTheme.typography.bodyLarge) {
                        content()
                    }
                    supportingContent?.let {
                        CompositionLocalProvider(
                            LocalContentColor provides contentColor.copy(alpha = 0.72f),
                        ) {
                            ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                                it()
                            }
                        }
                    }
                }

                trailingContent?.let {
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(contentAlignment = Alignment.Center) {
                        it()
                    }
                }
            }
        }
        return
    }

    val focusInteractionSource = interactionSource ?: remember { MutableInteractionSource() }
    val isFocused by focusInteractionSource.collectIsFocusedAsState()
    val focusScale by animateFloatAsState(
        targetValue = if (isFocused) 1.01f else 1f,
        label = "focusScale",
    )

    SegmentedListItem(
        modifier = modifier
            .zIndex(if (isFocused) 1f else 0f)
            .scale(focusScale)
            .then(
                if (isFocused) {
                    Modifier.border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = overrideShape,
                    )
                } else {
                    Modifier
                },
            ),
        selected = selected,
        onClick = onClick,
        onLongClick = onLongClick,
        enabled = enabled,
        verticalAlignment = Alignment.CenterVertically,
        shapes = remember(isFirstItem, isLastItem, shapes) {
            val defaultBaseShape = shapes.shape
            if (defaultBaseShape is CornerBasedShape) {
                shapes.copy(
                    shape = defaultBaseShape.copy(
                        topStart = overrideShape.topStart.takeIf { isFirstItem } ?: defaultBaseShape.topStart,
                        topEnd = overrideShape.topEnd.takeIf { isFirstItem } ?: defaultBaseShape.topEnd,
                        bottomStart = overrideShape.bottomStart.takeIf { isLastItem } ?: defaultBaseShape.bottomStart,
                        bottomEnd = overrideShape.bottomEnd.takeIf { isLastItem } ?: defaultBaseShape.bottomEnd,
                    ),
                )
            } else {
                shapes
            }
        },
        colors = colors,
        contentPadding = contentPadding,
        leadingContent = leadingContent,
        supportingContent = supportingContent,
        trailingContent = trailingContent,
        overlineContent = overlineContent,
        interactionSource = focusInteractionSource,
        content = content,
    )
}

@Composable
fun ListSectionTitle(
    modifier: Modifier = Modifier,
    text: String,
    contentPadding: PaddingValues = PaddingValues(
        start = 12.dp,
        top = 20.dp,
        bottom = 10.dp,
    ),
    color: Color = MaterialTheme.colorScheme.primary,
) {
    Text(
        text = text,
        modifier = modifier.padding(contentPadding),
        color = color,
        style = MaterialTheme.typography.labelLarge,
    )
}
