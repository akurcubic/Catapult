package com.example.catlistapp.cats.photo

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.catlistapp.cats.gallery.AppIconButton
import com.example.catlistapp.cats.gallery.CatGalleryContract

import com.example.catlistapp.cats.gallery.model.CatGalleryUiModel
import com.example.catlistapp.core.ourcomp.PhotoPreview

fun NavGraphBuilder.catPhoto(
    route: String,
    arguments: List<NamedNavArgument>,
    onClose: () -> Unit,
) = composable(
    route = route,
    arguments = arguments,
) { navBackStackEntry ->
    val catId = navBackStackEntry.arguments?.getString("catId")
        ?: throw IllegalStateException("catId required")

    val catPhotoViewModel = viewModel<CatPhotoViewModel>(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CatPhotoViewModel(catId = catId) as T
            }
        }
    )

    val state = catPhotoViewModel.state.collectAsState()

    CatPhotoScreen(
        state = state.value,
        onClose = onClose,
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CatPhotoScreen(
    state: CatGalleryContract.CatGalleryState,
    onClose: () -> Unit,
) {
    val pagerState = rememberPagerState(
        pageCount = { state.photos.size },
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Photo",
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    AppIconButton(
                        imageVector = Icons.Default.ArrowBack,
                        onClick = onClose,
                    )
                }
            )
        },
        content = { paddingValues ->
            if (state.photos.isNotEmpty()) {
                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = paddingValues,
                    pageSize = PageSize.Fill,
                    pageSpacing = 16.dp,
                    state = pagerState,
                    key = {
                        val photo = state.photos.getOrNull(it)
                        photo?.id ?: it
                    }
                ) { pageIndex ->
                    val photo = state.photos.getOrNull(pageIndex)
                    if (photo != null) {

                        Log.d("CatPhotoScreen", "Prikazujem sliku ${photo.id}")
                        PhotoPreview(
                            modifier = Modifier,
                            photo = photo,
                        )
                    } else {
                        // Loguj da slika nije pronađena za dati indeks
                        Log.e("CatPhotoScreen", "Slika nije pronađena za indeks $pageIndex")
                    }
                }
            } else {
                Text(
                    modifier = Modifier.fillMaxSize(),
                    text = "No photo.",
                )
            }
        },
    )
}


@Composable
@Preview(showBackground = true)
fun PreviewPhotoScreen() {
    val sampleAlbums = listOf(
        CatGalleryUiModel(id = "123", url = "aaa"),
        CatGalleryUiModel(id = "111", url = ""),
        CatGalleryUiModel(id = "222", url = ""),
        CatGalleryUiModel(id = "333", url = "")
    )

    val sampleState = CatGalleryContract.CatGalleryState(photos = sampleAlbums, catId = "absy")

    CatPhotoScreen(
        state = sampleState,
        onClose = {}
    )
}
