package com.example.catlistapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.catlistapp.cats.details.catDetails
import com.example.catlistapp.cats.gallery.catGallery
import com.example.catlistapp.cats.gallery.photo.catPhotoScreen
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
            route = "cat_details/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            }),
            navController = navController,
            onGalleryButtonClick = {
                navController.navigate(route = "cat_details/gallery/$it")
            }
        )

        catGallery(
            route = "cat_details/gallery/{id}",
            arguments = listOf(
                navArgument(name = "id") {
                    nullable = false
                    type = NavType.StringType
                }
            ),
            onPhotoClick = {id,photoIndex->
                navController.navigate(route = "photo/${id}/${photoIndex}")
            },
            onClose = {
                navController.navigateUp()
            }
        )


        catPhotoScreen(
            route = "photo/{id}/{photoIndex}",
            navController = navController,
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            }, navArgument("photoIndex") {
                type = NavType.IntType
            }),
        )

    }
}
