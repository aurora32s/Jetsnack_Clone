package com.haman.jetsnackclone.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.haman.jetsnackclone.R
import com.haman.jetsnackclone.ui.component.JetsnackDivider
import com.haman.jetsnackclone.ui.theme.AlphaNearOpaque
import com.haman.jetsnackclone.ui.theme.JetsnackCloneTheme
import com.haman.jetsnackclone.ui.theme.JetsnackTheme

@Composable
fun DestinationBar(modifier: Modifier = Modifier) {
    Column(modifier = modifier.statusBarsPadding()) {
        TopAppBar(
            backgroundColor = JetsnackTheme.colors.uiBackground.copy(alpha = AlphaNearOpaque),
            contentColor = JetsnackTheme.colors.textSecondary,
            elevation = 0.dp
        ) {
            Text(
                text = "Delivery to 1600 Amphitheater Way",
                style = MaterialTheme.typography.subtitle1,
                color = JetsnackTheme.colors.textSecondary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
            IconButton(
                onClick = { /* todo */ },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ExpandMore,
                    tint = JetsnackTheme.colors.brand,
                    contentDescription = stringResource(R.string.label_select_delivery)
                )
            }
        }
        JetsnackDivider()
    }
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
fun PreviewDestinationBar() {
    JetsnackCloneTheme {
        DestinationBar()
    }
}