package com.haman.jetsnackclone.ui.home.search

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.haman.jetsnackclone.model.SearchCategory
import com.haman.jetsnackclone.model.SearchCategoryCollection
import com.haman.jetsnackclone.model.SearchRepo
import com.haman.jetsnackclone.ui.component.SnackImage
import com.haman.jetsnackclone.ui.component.VerticalGrid
import com.haman.jetsnackclone.ui.theme.JetsnackCloneTheme
import com.haman.jetsnackclone.ui.theme.JetsnackTheme
import java.lang.Integer.max

@Composable
fun SearchCategories(
    categories: List<SearchCategoryCollection>
) {
    LazyColumn {
        itemsIndexed(items = categories) { index, collection ->
            SearchCategoryCollection(
                collection = collection,
                index = index
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun SearchCategoryCollection(
    collection: SearchCategoryCollection,
    index: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = collection.name,
            style = MaterialTheme.typography.h6,
            color = JetsnackTheme.colors.textPrimary,
            modifier = Modifier
                .heightIn(min = 56.dp)
                .padding(horizontal = 24.dp, vertical = 4.dp)
                .wrapContentHeight()
        )
        VerticalGrid(Modifier.padding(horizontal = 16.dp)) {
            val gradient = when (index % 2) {
                0 -> JetsnackTheme.colors.gradient2_2 // 짝수
                else -> JetsnackTheme.colors.gradient2_3 // 홏수
            }
            collection.categories.forEach { category ->
                SearchCategory(
                    category = category,
                    gradient = gradient,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        Spacer(Modifier.height(4.dp))
    }
}

private val MinImageSize = 134.dp
private val CategoryShape = RoundedCornerShape(10.dp)
private const val CategoryTextProportion = 0.6f

@Composable
fun SearchCategory(
    category: SearchCategory,
    gradient: List<Color>,
    modifier: Modifier = Modifier
) {
    Layout(
        modifier = modifier
            .aspectRatio(1.45f)
            .shadow(elevation = 3.dp, shape = CategoryShape)
            .clip(CategoryShape)
            .background(Brush.horizontalGradient(gradient))
            .clickable { },
        content = {
            Text(
                text = category.name,
                style = MaterialTheme.typography.subtitle1,
                color = JetsnackTheme.colors.textSecondary,
                modifier = Modifier
                    .padding(4.dp)
                    .padding(start = 8.dp)
            )
            SnackImage(
                imageUrl = category.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    ) { measurables, constraints ->
        val textWidth = (constraints.maxWidth * CategoryTextProportion).toInt()
        val textPlaceable = measurables[0].measure(Constraints.fixedWidth(textWidth))

        val imageSize = max(MinImageSize.roundToPx(), constraints.maxHeight)
        val imagePlaceable = measurables[1].measure(Constraints.fixed(imageSize, imageSize))

        layout(
            constraints.maxWidth, constraints.minHeight
        ) {
            textPlaceable.placeRelative(0, (constraints.maxHeight - textPlaceable.height) / 2)
            imagePlaceable.placeRelative(
                textWidth,
                (constraints.maxHeight - imagePlaceable.height) / 2
            )
        }
    }
}

@Preview("search category", showBackground = true)
@Preview("search category dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchCategoryCollectionPreview() {
    JetsnackCloneTheme {
        SearchCategoryCollection(
            collection = SearchRepo.getCategories().first(),
            index = 0
        )
    }
}

@Preview("search category", showBackground = true)
@Preview("search category dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchCategoryCollectionEvenIndexPreview() {
    JetsnackCloneTheme {
        SearchCategoryCollection(
            collection = SearchRepo.getCategories().first(),
            index = 1
        )
    }
}

@Preview("search category", showBackground = true)
@Preview("search category dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchCategoriesPreview() {
    JetsnackCloneTheme {
        SearchCategories(
            categories = SearchRepo.getCategories()
        )
    }
}