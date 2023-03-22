package com.haman.jetsnackclone.ui.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haman.jetsnackclone.R
import com.haman.jetsnackclone.ui.theme.JetsnackCloneTheme
import com.haman.jetsnackclone.ui.theme.JetsnackTheme

@Composable
fun QuantitySelector(
    modifier: Modifier = Modifier,
    count: Int,
    decreaseItemCount: () -> Unit,
    increaseItemCount: () -> Unit
) {
    Row(modifier = modifier) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = stringResource(id = R.string.quantity),
                style = MaterialTheme.typography.subtitle1,
                color = JetsnackTheme.colors.textSecondary,
                modifier = Modifier
                    .padding(end = 18.dp)
                    .align(Alignment.CenterVertically)
            )
        }

        JetsnackGradientTintedIconButton(
            imageVector = Icons.Default.Remove,
            onClick = decreaseItemCount,
            contentDescription = stringResource(id = R.string.label_decrease),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Crossfade(
            targetState = count,
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                text = "$it",
                style = MaterialTheme.typography.subtitle2,
                fontSize = 18.sp,
                color = JetsnackTheme.colors.textPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(24.dp)
            )
        }
        JetsnackGradientTintedIconButton(
            imageVector = Icons.Default.Add,
            onClick = increaseItemCount,
            contentDescription = stringResource(id = R.string.label_increase),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Preview("default")
@Preview("dark theme", uiMode = UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
fun QuantitySelectorPreview() {
    JetsnackCloneTheme {
        JetsnackSurface {
            QuantitySelector(count = 1, decreaseItemCount = {}, increaseItemCount = {})
        }
    }
}
