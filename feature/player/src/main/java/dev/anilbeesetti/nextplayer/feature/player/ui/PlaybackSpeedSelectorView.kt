package dev.anilbeesetti.nextplayer.feature.player.ui

import androidx.annotation.OptIn
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import dev.anilbeesetti.nextplayer.core.common.extensions.round
import dev.anilbeesetti.nextplayer.core.ui.R
import dev.anilbeesetti.nextplayer.core.ui.components.NextSwitch
import dev.anilbeesetti.nextplayer.core.ui.glass.LiquidGlassSlider
import dev.anilbeesetti.nextplayer.core.ui.glass.LiquidGlassStyle
import dev.anilbeesetti.nextplayer.core.ui.glass.LocalLiquidGlassBackdrop
import dev.anilbeesetti.nextplayer.core.ui.glass.liquidGlass
import dev.anilbeesetti.nextplayer.feature.player.buttons.PlayerButton
import dev.anilbeesetti.nextplayer.feature.player.state.rememberPlaybackParametersState
import kotlin.math.abs

@OptIn(UnstableApi::class)
@Composable
fun BoxScope.PlaybackSpeedSelectorView(
    modifier: Modifier = Modifier,
    show: Boolean,
    player: Player,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val playbackParametersState = rememberPlaybackParametersState(player)
    val glassBackdrop = LocalLiquidGlassBackdrop.current

    OverlayView(
        modifier = modifier,
        show = show,
        title = stringResource(R.string.select_playback_speed),
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            val minValue = 0.2f
            val maxValue = 4.0f
            val stepSize = 0.1f
            val steps = ((maxValue - minValue) / stepSize).toInt() - 1
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                PlayerButton(
                    modifier = Modifier.size(42.dp),
                    onClick = {
                        val newSpeed =
                            (playbackParametersState.speed - stepSize).coerceAtLeast(minValue)
                        playbackParametersState.setPlaybackSpeed(newSpeed)
                    },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_remove),
                        contentDescription = null,
                    )
                }

                Text(
                    text = playbackParametersState.speed.round(2).toString(),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = if (glassBackdrop != null) Color.White else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )

                PlayerButton(
                    modifier = Modifier.size(42.dp),
                    onClick = {
                        val newSpeed = (playbackParametersState.speed + stepSize).coerceAtMost(maxValue)
                        playbackParametersState.setPlaybackSpeed(newSpeed)
                    },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = null,
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                LiquidGlassSlider(
                    value = playbackParametersState.speed,
                    valueRange = minValue..maxValue,
                    steps = steps,
                    onValueChange = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        playbackParametersState.setPlaybackSpeed(it)
                    },
                    activeColor = if (glassBackdrop != null) Color.White else Color.Unspecified,
                    modifier = Modifier.weight(1f),
                )
                PlayerButton(
                    modifier = Modifier.size(42.dp),
                    onClick = { playbackParametersState.setPlaybackSpeed(1f) },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_reset),
                        contentDescription = null,
                    )
                }
            }
            FlowRow(
                maxItemsInEachRow = 5,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                listOf(
                    0.2f, 0.5f, 0.75f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f,
                ).forEach { speed ->
                    val selected = abs(playbackParametersState.speed - speed) < 0.051f
                    val chipModifier = if (glassBackdrop != null) {
                        Modifier.liquidGlass(
                            backdrop = glassBackdrop,
                            style = LiquidGlassStyle.CONTROL,
                            shape = CircleShape,
                            surfaceColor = if (selected) {
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.38f)
                            } else {
                                Color.Black.copy(alpha = 0.12f)
                            },
                        )
                    } else {
                        Modifier
                            .clip(CircleShape)
                            .border(
                                width = 1.dp,
                                color = LocalContentColor.current,
                                shape = CircleShape,
                            )
                    }

                    Box(
                        modifier = chipModifier
                            .clickable { playbackParametersState.setPlaybackSpeed(speed) }
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                            .weight(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = speed.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (glassBackdrop != null) {
                                Color.White
                            } else {
                                MaterialTheme.colorScheme.primary
                            },
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .toggleable(
                        value = playbackParametersState.skipSilenceEnabled,
                        onValueChange = { playbackParametersState.setIsSkipSilenceEnabled(it) },
                    )
                    .fillMaxWidth()
                    .padding(8.dp)
                    .semantics(mergeDescendants = true) {},
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = stringResource(R.string.skip_silence),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (glassBackdrop != null) Color.White else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )
                NextSwitch(
                    checked = playbackParametersState.skipSilenceEnabled,
                    onCheckedChange = null,
                )
            }
        }
    }
}
