package com.haman.jetsnackclone.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import com.haman.jetsnackclone.ui.component.JetsnackScaffold
import com.haman.jetsnackclone.ui.theme.JetsnackCloneTheme

@Composable
fun JetsnackApp() {
    JetsnackCloneTheme {
        val appState = rememberJetsnackAppState()
        JetsnackScaffold(
            bottomBar = {},
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
}