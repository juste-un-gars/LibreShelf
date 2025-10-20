package com.libreshelf.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.libreshelf.presentation.screens.libraries.LibrariesScreen
import com.libreshelf.presentation.screens.library.LibraryDetailScreen
import com.libreshelf.presentation.screens.settings.SettingsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Libraries.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Libraries.route) {
            LibrariesScreen(
                onNavigateToLibrary = { libraryId ->
                    navController.navigate(Screen.LibraryDetail.createRoute(libraryId))
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(
            route = Screen.LibraryDetail.route,
            arguments = listOf(navArgument("libraryId") { type = NavType.LongType })
        ) { backStackEntry ->
            val libraryId = backStackEntry.arguments?.getLong("libraryId") ?: return@composable
            LibraryDetailScreen(
                libraryId = libraryId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToBook = { bookId ->
                    navController.navigate(Screen.Reader.createRoute(bookId))
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // TODO: Add more screens (Reader, BookDetail, etc.)
    }
}
