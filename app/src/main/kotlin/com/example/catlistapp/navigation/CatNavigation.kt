package com.example.catlistapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.catlistapp.cats.details.catDetails
import com.example.catlistapp.cats.list.cats

@Composable
fun CatNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "cats"
    ) {
        cats(
            route = "cats",
            onCatClick = { catId ->
                navController.navigate(route = "cat_details/$catId")
            }
        )
        catDetails(
            route = "cat_details/{catId}",
            navController = navController
        )
    }
}
