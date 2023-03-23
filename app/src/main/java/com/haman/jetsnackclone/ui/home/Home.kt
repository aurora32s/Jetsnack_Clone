package com.haman.jetsnackclone.ui.home

import androidx.annotation.FloatRange
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.haman.jetsnackclone.R
import com.haman.jetsnackclone.ui.component.JetsnackSurface
import com.haman.jetsnackclone.ui.theme.JetsnackCloneTheme
import com.haman.jetsnackclone.ui.theme.JetsnackTheme

fun NavGraphBuilder.addHomeGraph(
    onSnackSelected: (Long, NavBackStackEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    composable(HomeSections.FEED.route) { from ->
        Feed(onSnackClick = { id -> onSnackSelected(id, from) }, modifier = modifier)
    }
    composable(HomeSections.PROFILE.route) { Profile() }
}

enum class HomeSections(
    @StringRes val title: Int,
    val icon: ImageVector,
    val route: String
) {
    FEED(R.string.home_feed, Icons.Outlined.Home, "home/feed"),
    SEARCH(R.string.home_search, Icons.Outlined.Search, "home/search"),
    CART(R.string.home_cart, Icons.Outlined.ShoppingCart, "home/cart"),
    PROFILE(R.string.home_profile, Icons.Outlined.AccountCircle, "home/profile")
}

@Composable
fun JetsnackBottomBar(
    tabs: Array<HomeSections>,
    currentRoute: String,
    navigateToRoute: (String) -> Unit,
    color: Color = JetsnackTheme.colors.iconPrimary,
    contentColor: Color = JetsnackTheme.colors.iconInteractive
) {
    val routes = remember { tabs.map { it.route } }
    val currentSection = tabs.first { it.route == currentRoute }

    JetsnackSurface(
        color = color,
        contentColor = contentColor
    ) {
        val springSpec = SpringSpec<Float>(
            stiffness = 800f,
            dampingRatio = 0.8f
        )

        JetsnackBottomNavLayout(
            selectedIndex = currentSection.ordinal,
            itemCount = routes.size,
            animSpec = springSpec,
            indicator = { JetsnackBottomNavIndicator() },
            modifier = Modifier.navigationBarsPadding()
        ) {
            tabs.forEach { tab ->
                val selected = tab == currentSection
                val tint by animateColorAsState(
                    if (selected) {
                        JetsnackTheme.colors.iconInteractive
                    } else {
                        JetsnackTheme.colors.iconInteractiveInactive
                    }
                )

                val text = stringResource(id = tab.title)
                JetsnackBottomNavItem(
                    icon = {
                        Icon(
                            imageVector = tab.icon,
                            tint = tint,
                            contentDescription = text
                        )
                    },
                    text = {
                        Text(
                            text = text,
                            color = tint,
                            style = MaterialTheme.typography.button,
                            maxLines = 1
                        )
                    },
                    selected = selected,
                    onSelected = { navigateToRoute(tab.route) },
                    animSpec = springSpec,
                    modifier = bottomNavigationItemPadding
                        .clip(bottomNavIndicatorShape)
                )
            }
        }
    }
}

@Composable
private fun JetsnackBottomNavLayout(
    selectedIndex: Int,
    itemCount: Int,
    animSpec: AnimationSpec<Float>,
    indicator: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    // 초기 Animate 상태 설정
    val selectionFractions = remember(itemCount) {
        List(itemCount) { i -> Animatable(if (selectedIndex == i) 1f else 0f) }
    }
    // selectedIndex 가 변경되면 해당 item 은 1f 로 그 외는 0f 로 변경
    selectionFractions.forEachIndexed { index, animatable ->
        val target = if (index == selectedIndex) 1f else 0f
        LaunchedEffect(key1 = target, key2 = animSpec) {
            animatable.animateTo(target, animSpec)
        }
    }

    // Indicator animation 을 위해 Animatable 사용
    val indicatorIndex = remember { Animatable(0f) }
    val targetIndicatorIndex = selectedIndex.toFloat()
    LaunchedEffect(key1 = targetIndicatorIndex) {
        indicatorIndex.animateTo(targetIndicatorIndex, animSpec)
    }

    Layout(
        modifier = modifier.height(BottomNavHeight),
        content = {
            content()
            Box(
                modifier = Modifier.layoutId("indicator"),
                content = indicator
            )
        }
    ) { measurables, constraints ->
        check(itemCount == (measurables.size - 1))

        // 선택 안된 item width : 선택된 item width = 1 : 2
        val unselectedWidth = constraints.maxWidth / (itemCount + 1)
        val selectedWidth = 2 * unselectedWidth
        val indicatorMeasurable = measurables.first { it.layoutId == "indicator" }

        val itemPlaceables = measurables
            .filterNot { it == indicatorMeasurable } // indicator를 제외하고 measure
            .mapIndexed { index, measurable ->
                val width = lerp(
                    start = unselectedWidth,
                    stop = selectedWidth,
                    fraction = selectionFractions[index].value
                )
                measurable.measure(
                    constraints.copy(
                        minWidth = width,
                        maxWidth = width
                    )
                )
            }
        val indicatorPlaceable = indicatorMeasurable.measure(
            constraints.copy(
                minWidth = selectedWidth,
                maxWidth = selectedWidth
            )
        )

        layout(
            width = constraints.maxWidth,
            height = itemPlaceables.maxOfOrNull { it.height } ?: constraints.minHeight
        ) {
            val indicatorLeft = indicatorIndex.value * unselectedWidth
            indicatorPlaceable.placeRelative(x = indicatorLeft.toInt(), y = 0)
            var x = 0
            itemPlaceables.forEach { placeable ->
                placeable.placeRelative(x = x, y = 0)
                x += placeable.width
            }
        }
    }
}

private val BottomNavHeight = 56.dp
private val TextIconSpacing = 2.dp
private val BottomNavLabelTransformOrigin = TransformOrigin(0f, 0.5f)

/**
 * Bottom tab Item
 */
@Composable
private fun JetsnackBottomNavItem(
    icon: @Composable BoxScope.() -> Unit,
    text: @Composable BoxScope.() -> Unit,
    selected: Boolean,
    onSelected: () -> Unit,
    animSpec: AnimationSpec<Float>,
    modifier: Modifier = Modifier
) {
    val animationProgress by animateFloatAsState(if (selected) 1f else 0f, animSpec)
    JetsnackBottomNavItemLayout(
        icon = icon,
        text = text,
        animateProgress = animationProgress,
        modifier = modifier
            .selectable(selected = selected, onClick = onSelected)
            .wrapContentSize()
    )
}

@Composable
private fun JetsnackBottomNavItemLayout(
    icon: @Composable BoxScope.() -> Unit,
    text: @Composable BoxScope.() -> Unit,
    @FloatRange(from = 0.0, to = 1.0) animateProgress: Float,
    modifier: Modifier = Modifier
) {
    Layout(
        modifier = modifier,
        content = {
            Box(
                modifier = Modifier
                    .layoutId("icon")
                    .padding(horizontal = TextIconSpacing),
                content = icon
            )
            val scale = lerp(
                start = 0.6f,
                stop = 1f,
                fraction = animateProgress
            )
            Box(
                modifier = Modifier
                    .layoutId("text")
                    .padding(horizontal = TextIconSpacing)
                    .graphicsLayer {
                        alpha = animateProgress
                        scaleX = scale
                        scaleY = scale
                        // TODO what is transformOrigin
                        transformOrigin = BottomNavLabelTransformOrigin
                    },
                content = text
            )
        }
    ) { measureables, constraints ->
        val iconPlaceable = measureables.first { it.layoutId == "icon" }.measure(constraints)
        val textPlaceable = measureables.first { it.layoutId == "text" }.measure(constraints)

        placeTextAndIcon(
            iconPlaceable = iconPlaceable,
            textPlaceable = textPlaceable,
            width = constraints.maxWidth,
            height = constraints.maxHeight,
            animateProgress = animateProgress
        )
    }
}

/**
 * animateProgress 에 따라 text showing
 */
private fun MeasureScope.placeTextAndIcon(
    textPlaceable: Placeable,
    iconPlaceable: Placeable,
    width: Int,
    height: Int,
    @FloatRange(from = 0.0, to = 1.0) animateProgress: Float
): MeasureResult {
    val iconY = (height - iconPlaceable.height) / 2
    val textY = (height - textPlaceable.height) / 2

    val textWidth = textPlaceable.width * animateProgress
    val iconX = (width - textWidth - iconPlaceable.width) / 2
    val textX = iconX + iconPlaceable.width

    return layout(width, height) {
        iconPlaceable.placeRelative(iconX.toInt(), iconY)
        if (animateProgress != 0f) {
            textPlaceable.placeRelative(textX.toInt(), textY)
        }
    }
}

private val bottomNavIndicatorShape = RoundedCornerShape(percent = 50)
private val bottomNavigationItemPadding = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)

@Composable
private fun JetsnackBottomNavIndicator(
    strokeWidth: Dp = 2.dp,
    color: Color = JetsnackTheme.colors.iconInteractive,
    shape: Shape = bottomNavIndicatorShape
) {
    Spacer(
        modifier = Modifier
            .fillMaxSize()
            .then(bottomNavigationItemPadding)
            .border(
                width = strokeWidth,
                color = color,
                shape = shape
            )
    )
}

@Preview
@Composable
private fun JetsnackBottomNavPreview() {
    JetsnackCloneTheme {
        JetsnackBottomBar(
            tabs = HomeSections.values(),
            currentRoute = "home/feed",
            navigateToRoute = { }
        )
    }
}