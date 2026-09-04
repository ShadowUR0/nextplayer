package dev.anilbeesetti.nextplayer.settings.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.anilbeesetti.nextplayer.core.model.ControlButtonsPosition
import dev.anilbeesetti.nextplayer.core.ui.R

@Composable
fun ControlButtonsPosition.name(): String {
    return when (this) {
        ControlButtonsPosition.LEFT -> stringResource(R.string.control_buttons_alignment_left)
        ControlButtonsPosition.CENTER -> "Center"
        ControlButtonsPosition.RIGHT -> stringResource(R.string.control_buttons_alignment_right)
    }
}
