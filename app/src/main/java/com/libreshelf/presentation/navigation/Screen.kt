package com.libreshelf.presentation.navigation

sealed class Screen(val route: String) {
    object Libraries : Screen("libraries")
    object LibraryDetail : Screen("library/{libraryId}") {
        fun createRoute(libraryId: Long) = "library/$libraryId"
    }
    object BookDetail : Screen("book/{bookId}") {
        fun createRoute(bookId: Long) = "book/$bookId"
    }
    object Reader : Screen("reader/{bookId}") {
        fun createRoute(bookId: Long) = "reader/$bookId"
    }
    object RecentBooks : Screen("recent")
    object FavoriteBooks : Screen("favorites")
    object Settings : Screen("settings")
    object NetworkSources : Screen("network_sources")
    object AddBook : Screen("add_book/{libraryId}") {
        fun createRoute(libraryId: Long) = "add_book/$libraryId"
    }
}
