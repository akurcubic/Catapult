package com.example.catlistapp.cats.gallery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.SubcomposeAsyncImage
import com.example.catlistapp.cats.entities.CatGallery


fun NavGraphBuilder.catGallery(
    route: String,
    arguments: List<NamedNavArgument>,
    onPhotoClick: (catId: String, photoIndex: Int) -> Unit,
    onClose: () -> Unit,
) = composable(
    route = route,
    arguments = arguments,
) { navBackStackEntry ->

    val catGalleryViewModel: CatGalleryViewModel = hiltViewModel(navBackStackEntry)


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
    onPhotoClick: (catId: String, photoIndex: Int) -> Unit,
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
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Fixed(2),
                    contentPadding = paddingValues
                ) {
                    itemsIndexed(
                        items = state.photos,
                        key = { index: Int, item: CatGallery ->
                            item.url
                        }
                    ) { index: Int, item: CatGallery ->
                        SubcomposeAsyncImage(
                            model = item.url,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clip(RectangleShape)
                                .clickable {
                                    onPhotoClick(state.catId, index)
                                },
                            contentScale = ContentScale.Crop,
                            loading = {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            },
                            error = {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    // Display some error UI if needed
                                }
                            }
                        )
                    }
                }

//                LazyVerticalGrid(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(horizontal = 4.dp),
//                    columns = GridCells.Fixed(2),
//                    contentPadding = paddingValues,
//                    verticalArrangement = Arrangement.spacedBy(4.dp),
//                    horizontalArrangement = Arrangement.spacedBy(4.dp),
//                ) {
//                    itemsIndexed(
//                        items = state.photos,
//                        key = { index: Int, photo: CatGalleryUiModel ->
//                            photo.id
//                        },
//                    ) { index: Int, photo: CatGalleryUiModel ->
//                        Card(
//                            modifier = Modifier
//                                .size(cellSize)
//                                .clickable {
//                                    onPhotoClick(state.catId)
//                                },
//                        ) {
//                            PhotoPreview(
//                                modifier = Modifier.fillMaxSize(),
//                                photo = photo,
//                            )
//                        }
//                    }
//                }
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




//@Composable
//@Preview(showBackground = true)
//fun PreviewCatGalleryScreen() {
//
//    val sampleAlbums = listOf(
//        CatGalleryUiModel(id = "123", url = ""),
//        CatGalleryUiModel(id = "111", url = ""),
//        CatGalleryUiModel(id = "222", url = ""),
//        CatGalleryUiModel(id = "333", url = "")
//    )
//
//    val sampleState = CatGalleryContract.CatGalleryState(photos = sampleAlbums, catId = "absy")
//
//    CatGalleryScreen(
//        state = sampleState,
//        onPhotoClick = {
//
//        },
//        onClose = {
//
//        }
//    )
//}


