package com.haman.jetsnackclone.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.haman.jetsnackclone.model.Filter
import com.haman.jetsnackclone.model.SnackCollection
import com.haman.jetsnackclone.model.SnackRepo
import com.haman.jetsnackclone.ui.component.FilterBar
import com.haman.jetsnackclone.ui.component.JetsnackDivider
import com.haman.jetsnackclone.ui.component.JetsnackSurface
import com.haman.jetsnackclone.ui.component.SnackCollection
import com.haman.jetsnackclone.ui.theme.JetsnackCloneTheme

@Composable
fun Feed(
    onSnackClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val snackCollections = remember { SnackRepo.getSnacks() }
    val filters = remember { SnackRepo.getFilters() }
    Feed(
        snackCollections = snackCollections,
        filters = filters,
        onSnackClick = onSnackClick,
        modifier = modifier
    )
}

@Composable
fun Feed(
    snackCollections: List<SnackCollection>,
    filters: List<Filter>,
    onSnackClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    JetsnackSurface(modifier = modifier.fillMaxSize()) {
        Box {
            SnackCollectionList(
                snackCollections = snackCollections,
                filters = filters,
                onSnackClick = onSnackClick
            )
        }
    }
}

@Composable
private fun SnackCollectionList(
    snackCollections: List<SnackCollection>,
    filters: List<Filter>,
    onSnackClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val filterVisible = rememberSaveable { mutableStateOf(false) }
    Box(modifier = modifier) {
        LazyColumn {
            item {
                Spacer(
                    modifier = Modifier.windowInsetsTopHeight(
                        WindowInsets.statusBars.add(
                            WindowInsets(top = 56.dp)
                        )
                    )
                )
                FilterBar(filters = filters, onShowFilters = { filterVisible.value = true })
            }
            itemsIndexed(items = snackCollections) { index, snackCollection ->
                if (index > 0) {
                    JetsnackDivider(thickness = 2.dp)
                }
                SnackCollection(
                    snackCollection = snackCollection,
                    onSnackClick = onSnackClick,
                    index = index
                )
            }
        }
    }
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
fun HomePreview() {
    JetsnackCloneTheme() {
        Feed(onSnackClick = { })
    }
}