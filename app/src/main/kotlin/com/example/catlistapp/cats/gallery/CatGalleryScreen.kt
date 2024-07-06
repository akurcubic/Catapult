package com.example.catlistapp.cats.gallery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

import com.example.catlistapp.cats.gallery.model.CatGalleryUiModel
import com.example.catlistapp.core.ourcomp.PhotoPreview

fun NavGraphBuilder.catGallery(
    route: String,
    arguments: List<NamedNavArgument>,
    // proveri da li se prosledju id od macke ili sta vec, trenutno se prosldjuje id slike
    onPhotoClick: (String) -> Unit,
    onClose: () -> Unit,
) = composable(
    route = route,
    arguments = arguments,
) { navBackStackEntry ->
    val catId = navBackStackEntry.arguments?.getString("catId")
        ?: throw IllegalStateException("catId required")

    val catGalleryViewModel = viewModel<CatGalleryViewModel>(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CatGalleryViewModel(catId = catId) as T
            }
        }
    )

    val state = catGalleryViewModel.state.collectAsState()

    CatGalleryScreen(
        state = state.value,
        onPhotoClick = onPhotoClick,
        onClose = onClose,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatGalleryScreen(
    state: CatGalleryContract.CatGalleryState,
    onPhotoClick: (catId: String) -> Unit,
    onClose: () -> Unit,
) {
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text(text = "Gallery") },
                navigationIcon = {
                    AppIconButton(
                        imageVector = Icons.Default.ArrowBack,
                        onClick = onClose,
                    )
                }
            )
        },
        content = { paddingValues ->
            BoxWithConstraints(
                modifier = Modifier,
                contentAlignment = Alignment.BottomCenter,
            ) {
                val screenWidth = this.maxWidth
                val cellSize = (screenWidth / 2) - 4.dp

                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp),
                    columns = GridCells.Fixed(2),
                    contentPadding = paddingValues,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    itemsIndexed(
                        items = state.photos,
                        key = { index: Int, photo: CatGalleryUiModel ->
                            photo.id
                        },
                    ) { index: Int, photo: CatGalleryUiModel ->
                        Card(
                            modifier = Modifier
                                .size(cellSize)
                                .clickable {
                                    onPhotoClick(state.catId)
                                },
                        ) {
                            PhotoPreview(
                                modifier = Modifier.fillMaxSize(),
                                photo = photo,
                            )
                        }
                    }
                }
            }
        },
    )
}

@Composable
fun AppIconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    onClick: () -> Unit,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current,
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint,
        )
    }
}




@Composable
@Preview(showBackground = true)
fun PreviewCatGalleryScreen() {

    val sampleAlbums = listOf(
        CatGalleryUiModel(id = "123", url = ""),
        CatGalleryUiModel(id = "111", url = ""),
        CatGalleryUiModel(id = "222", url = ""),
        CatGalleryUiModel(id = "333", url = "")
    )

    val sampleState = CatGalleryContract.CatGalleryState(photos = sampleAlbums, catId = "absy")

    CatGalleryScreen(
        state = sampleState,
        onPhotoClick = {

        },
        onClose = {

        }
    )
}


