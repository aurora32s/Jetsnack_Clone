package com.haman.jetsnackclone.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.haman.jetsnackclone.ui.theme.JetsnackCloneTheme
import com.haman.jetsnackclone.ui.theme.JetsnackTheme

private val ButtonShape = RoundedCornerShape(percent = 50)

@Composable
fun JetsnackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = ButtonShape,
    border: BorderStroke? = null,
    backgroundGradient: List<Color> = JetsnackTheme.colors.interactivePrimary,
    disableBackgroundGradient: List<Color> = JetsnackTheme.colors.interactiveSecondary,
    contentColor: Color = JetsnackTheme.colors.textInteractive,
    disableContentColor: Color = JetsnackTheme.colors.textHelp,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    JetsnackSurface(
        shape = shape,
        color = Color.Transparent,
        contentColor = if (enabled) contentColor else disableContentColor,
        border = border,
        modifier = modifier
            .clip(shape)
            .background(
                Brush.horizontalGradient(
                    colors = if (enabled) backgroundGradient else disableBackgroundGradient
                )
            )
            .clickable(
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.button) {
            Row(
                modifier = Modifier
                    .defaultMinSize(
                        minWidth = ButtonDefaults.MinWidth,
                        minHeight = ButtonDefaults.MinHeight
                    )
                    .indication(interactionSource, rememberRipple())
                    .padding(contentPadding),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}

@Preview(name = "default")
@Preview(name = "dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "large font", fontScale = 2f)
@Composable
private fun ButtonPreview() {
    JetsnackCloneTheme {
        JetsnackButton(onClick = {}) {
            Text(text = "Demo")
        }
    }
}

@Preview(name = "default")
@Preview(name = "dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "large font", fontScale = 2f)
@Composable
private fun RectangleButtonPreview() {
    JetsnackCloneTheme {
        JetsnackButton(
            onClick = {},
            shape = RectangleShape
        ) {
            Text(text = "Demo")
        }
    }
}