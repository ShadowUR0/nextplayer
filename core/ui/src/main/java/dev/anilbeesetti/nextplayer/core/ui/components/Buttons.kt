package dev.anilbeesetti.nextplayer.core.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.anilbeesetti.nextplayer.core.ui.R
import dev.anilbeesetti.nextplayer.core.ui.glass.LiquidGlassButton
import dev.anilbeesetti.nextplayer.core.ui.glass.LocalLiquidGlassBackdrop

@Composable
fun DoneButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val backdrop = LocalLiquidGlassBackdrop.current
    if (backdrop != null) {
        LiquidGlassButton(
            enabled = enabled,
            onClick = onClick,
            modifier = modifier.tvFocusRing(shape = RoundedCornerShape(50)),
            shape = RoundedCornerShape(50),
        ) {
            Text(text = stringResource(R.string.done))
        }
    } else {
        TextButton(
            enabled = enabled,
            onClick = onClick,
            modifier = modifier.tvFocusRing(shape = RoundedCornerShape(50)),
        ) {
            Text(text = stringResource(R.string.done))
        }
    }
}

@Composable
fun CancelButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val backdrop = LocalLiquidGlassBackdrop.current
    if (backdrop != null) {
        LiquidGlassButton(
            enabled = enabled,
            onClick = onClick,
            modifier = modifier.tvFocusRing(shape = RoundedCornerShape(50)),
            shape = RoundedCornerShape(50),
        ) {
            Text(text = stringResource(R.string.cancel))
        }
    } else {
        TextButton(
            enabled = enabled,
            onClick = onClick,
            modifier = modifier.tvFocusRing(shape = RoundedCornerShape(50)),
        ) {
            Text(text = stringResource(R.string.cancel))
        }
    }
}
