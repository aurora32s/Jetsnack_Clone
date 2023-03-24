package com.haman.jetsnackclone.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.haman.jetsnackclone.ui.component.JetsnackScaffold
import com.haman.jetsnackclone.ui.home.HomeSections
import com.haman.jetsnackclone.ui.home.JetsnackBottomBar
import com.haman.jetsnackclone.ui.home.addHomeGraph
import com.haman.jetsnackclone.ui.snackdetail.SnackDetail
import com.haman.jetsnackclone.ui.theme.JetsnackCloneTheme

@Composable
fun JetsnackApp() {
    JetsnackCloneTheme {
        val appState = rememberJetsnackAppState()
        JetsnackScaffold(
            bottomBar = {
                if (appState.shouldShowBottomBar) {
                    JetsnackBottomBar(
                        tabs = appState.bottomBarTabs,
                        currentRoute = appState.currentRoute!!,
                        navigateToRoute = appState::navigateToBottomBarRoute
                    )
                }
            },
            snackbarHost = {},
            scaffoldState = appState.scaffoldState
        ) { innerPadding ->
            NavHost(
                navController = appState.navController,
                startDestination = MainDestinations.HOME_ROUTE,
                modifier = Modifier.padding(innerPadding)
            ) {
                jetsnackNavGraph(
                    onSnackSelected = appState::navigateToSnackDetail,
                    upPress = appState::upPress
                )
            }
        }
    }
}

private fun NavGraphBuilder.jetsnackNavGraph(
    onSnackSelected: (Long, NavBackStackEntry) -> Unit,
    upPress: () -> Unit
) {
    navigation(
        route = MainDestinations.HOME_ROUTE,
        startDestination = HomeSections.FEED.route
    ) {
        addHomeGraph(onSnackSelected)
    }

    composable(
        route = "${MainDestinations.SNACK_DETAIL_ROUTE}/{${MainDestinations.SNACK_ID_KEY}}",
        arguments = listOf(navArgument(MainDestinations.SNACK_ID_KEY) { type = NavType.LongType })
    ) { navBackStackEntry ->
        val snackId =
            requireNotNull(navBackStackEntry.arguments).getLong(MainDestinations.SNACK_ID_KEY)
        SnackDetail(
            snackId = snackId,
            upPress = upPress
        )
    }
}