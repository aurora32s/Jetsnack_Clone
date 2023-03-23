package com.haman.jetsnackclone.ui.home.cart

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.haman.jetsnackclone.R
import com.haman.jetsnackclone.model.OrderLine
import com.haman.jetsnackclone.model.SnackCollection
import com.haman.jetsnackclone.model.SnackRepo
import com.haman.jetsnackclone.model.SnackbarManager
import com.haman.jetsnackclone.ui.component.*
import com.haman.jetsnackclone.ui.home.DestinationBar
import com.haman.jetsnackclone.ui.theme.JetsnackCloneTheme
import com.haman.jetsnackclone.ui.theme.JetsnackTheme

@Composable
fun Cart(
    onSnackClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CartViewModel = CartViewModel(SnackbarManager, SnackRepo)
) {
    val orderLines = viewModel.orderLines.collectAsState()
    val inspiredByCart = remember { SnackRepo.getInspiredByCart() }
    Cart(
        orderLines = orderLines.value,
        removeSnack = viewModel::removeSnack,
        increaseItem = viewModel::increaseSnackCount,
        decreaseItem = viewModel::decreaseSnackCount,
        inspiredByCart = inspiredByCart,
        onSnackClick = onSnackClick,
        modifier = modifier
    )
}

@Composable
fun Cart(
    orderLines: List<OrderLine>,
    removeSnack: (Long) -> Unit,
    increaseItem: (Long) -> Unit,
    decreaseItem: (Long) -> Unit,
    inspiredByCart: SnackCollection,
    onSnackClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    JetsnackSurface(modifier = modifier.fillMaxSize()) {
        Box {
            CartContent(
                orderLines = orderLines,
                removeSnack = removeSnack,
                increaseItemCount = increaseItem,
                decreaseItemCount = decreaseItem,
                inspiredByCart = inspiredByCart,
                onSnackClick = onSnackClick
            )
            DestinationBar(modifier = Modifier.align(Alignment.TopCenter))
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CartContent(
    orderLines: List<OrderLine>, // 주문 내역
    removeSnack: (Long) -> Unit, // 제거
    increaseItemCount: (Long) -> Unit,
    decreaseItemCount: (Long) -> Unit,
    inspiredByCart: SnackCollection,
    onSnackClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val resources = LocalContext.current.resources
    val snackCountFormattedString = remember(orderLines.size, resources) {
        resources.getQuantityString(
            R.plurals.cart_order_count,
            orderLines.size, orderLines.size
        )
    }
    LazyColumn(modifier) {
        item {
            Spacer(
                Modifier.windowInsetsTopHeight(
                    WindowInsets.statusBars.add(WindowInsets(top = 56.dp))
                )
            )
            Text(
                text = stringResource(R.string.cart_order_header, snackCountFormattedString),
                style = MaterialTheme.typography.h6,
                color = JetsnackTheme.colors.brand,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .heightIn(min = 56.dp)
                    .padding(horizontal = 24.dp, vertical = 4.dp)
                    .wrapContentHeight()
            )
        }
        items(orderLines) { orderLine ->
            SwipeDismissItem(
                background = { offsetX ->
                    /*Background color changes from light gray to red when the
                    swipe to delete with exceeds 160.dp*/
                    val backgroundColor = if (offsetX < -(160.dp)) {
                        JetsnackTheme.colors.error
                    } else {
                        JetsnackTheme.colors.uiFloated
                    }
                },
            ) {
                CartItem(
                    orderLine = orderLine,
                    removeSnack = removeSnack,
                    increaseItem = increaseItemCount,
                    decreaseItem = decreaseItemCount,
                    onSnackClick = onSnackClick
                )
            }
        }
        item {
            SnackCollection(
                snackCollection = inspiredByCart,
                onSnackClick = onSnackClick,
                highlight = false
            )
            Spacer(Modifier.height(56.dp))
        }
    }
}

@Composable
fun CartItem(
    orderLine: OrderLine,
    removeSnack: (Long) -> Unit,
    increaseItem: (Long) -> Unit,
    decreaseItem: (Long) -> Unit,
    onSnackClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val snack = orderLine.snack
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSnackClick(snack.id) }
            .background(JetsnackTheme.colors.uiBackground)
            .padding(horizontal = 24.dp)
    ) {
        val (divider, image, name, tag, priceSpacer, price, remove, quantity) = createRefs()
        createVerticalChain(name, tag, priceSpacer, price, chainStyle = ChainStyle.Packed)
        SnackImage(
            imageUrl = snack.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .constrainAs(image) {
                    top.linkTo(parent.top, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                }
        )
        Text(
            text = snack.name,
            style = MaterialTheme.typography.subtitle1,
            color = JetsnackTheme.colors.textSecondary,
            modifier = Modifier.constrainAs(name) {
                linkTo(
                    start = image.end,
                    startMargin = 16.dp,
                    end = remove.start,
                    endMargin = 16.dp,
                    bias = 0f
                )
            }
        )
        IconButton(
            onClick = { removeSnack(snack.id) },
            modifier = Modifier
                .constrainAs(remove) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .padding(top = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                tint = JetsnackTheme.colors.iconSecondary,
                contentDescription = stringResource(R.string.label_remove)
            )
        }
        Text(
            text = snack.tagline,
            style = MaterialTheme.typography.body1,
            color = JetsnackTheme.colors.textHelp,
            modifier = Modifier.constrainAs(tag) {
                linkTo(
                    start = image.end,
                    startMargin = 16.dp,
                    end = parent.end,
                    endMargin = 16.dp,
                    bias = 0f
                )
            }
        )
        Spacer(
            Modifier
                .height(8.dp)
                .constrainAs(priceSpacer) {
                    linkTo(top = tag.bottom, bottom = price.top)
                }
        )
        Text(
            text = snack.price.toString(),
            style = MaterialTheme.typography.subtitle1,
            color = JetsnackTheme.colors.textPrimary,
            modifier = Modifier.constrainAs(price) {
                linkTo(
                    start = image.end,
                    end = quantity.start,
                    startMargin = 16.dp,
                    endMargin = 16.dp,
                    bias = 0f
                )
            }
        )
        QuantitySelector(
            count = orderLine.count,
            decreaseItemCount = { decreaseItem(snack.id) },
            increaseItemCount = { increaseItem(snack.id) },
            modifier = Modifier.constrainAs(quantity) {
                baseline.linkTo(price.baseline)
                end.linkTo(parent.end)
            }
        )
        JetsnackDivider(
            Modifier.constrainAs(divider) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(parent.bottom)
            }
        )
    }
}

@Preview("default")
@Preview("dark theme", uiMode = UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
private fun CartPreview() {
    JetsnackCloneTheme {
        Cart(onSnackClick = {})
    }
}