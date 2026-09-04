package dev.anilbeesetti.nextplayer.core.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import dev.anilbeesetti.nextplayer.core.ui.glass.LiquidGlassStyle
import dev.anilbeesetti.nextplayer.core.ui.glass.LocalLiquidGlassBackdrop
import dev.anilbeesetti.nextplayer.core.ui.glass.liquidGlass

@Composable
fun RadioTextButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backdrop = LocalLiquidGlassBackdrop.current
    val shape = RoundedCornerShape(18.dp)
    val baseModifier = modifier.fillMaxWidth()
    val surfaceModifier = if (backdrop != null) {
        baseModifier.liquidGlass(
            backdrop = backdrop,
            style = LiquidGlassStyle.CONTROL,
            shape = shape,
            surfaceColor = if (selected) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
            } else {
                Color.Unspecified
            },
        )
    } else {
        baseModifier
    }

    Row(
        modifier = surfaceModifier
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LiquidGlassRadio(selected = selected)
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = text)
    }
}
