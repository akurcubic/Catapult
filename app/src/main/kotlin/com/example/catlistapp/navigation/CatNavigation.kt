package com.example.catlistapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.catlistapp.cats.details.catDetails
import com.example.catlistapp.cats.gallery.catGallery
import com.example.catlistapp.cats.list.cats
import com.example.catlistapp.cats.photo.catPhoto

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
            navController = navController,
            onGalleryButtonClick = {
                navController.navigate(route = "cat_details/gallery/$it")
            }
        )

        catGallery(
            route = "cat_details/gallery/{catId}",
            arguments = listOf(
                navArgument(name = "catId") {
                    nullable = false
                    type = NavType.StringType
                }
            ),
            onPhotoClick = {
                navController.navigate(route = "photo/$it")
            },
            onClose = {
                navController.navigateUp()
            }
        )

        catPhoto(
            route = "photo/{catId}",
            arguments = listOf(
                navArgument(name = "catId") {
                    nullable = false
                    type = NavType.StringType
                }
            ),
            onClose = {
                navController.navigateUp()
            }
        )

    }
}
