package com.example.catlistapp.cats.details

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.compose.rememberImagePainter
import com.example.catlistapp.cats.details.model.CatDetailsUiModel



fun NavGraphBuilder.catDetails(
    route: String,
    navController: NavController,
    arguments: List<NamedNavArgument>,
    onGalleryButtonClick: (String) -> Unit,
) = composable(
    route = route,
    arguments = arguments) { navBackStackEntry ->


    val catDetailsViewModel: CatDetailsViewModel = hiltViewModel(navBackStackEntry)

    val state = catDetailsViewModel.state.collectAsState()
    val context = LocalContext.current

    CatDetailsScreen(
        state = state.value,
        onClose = {
            navController.navigateUp()
        },
        context = context,
        onGalleryButtonClick = onGalleryButtonClick
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatDetailsScreen(
    state: CatDetailsContract.CatDetailsState,
    onClose: () -> Unit,
    context: Context,
    onGalleryButtonClick: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = state.cat?.name ?: "",
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF6200EE)
                ),
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            if (state.loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                state.cat?.let { cat ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .verticalScroll(rememberScrollState())
                            .background(Color(0xFFF0E7FF))
                            .padding(16.dp)
                    ) {
                        // Prikaz slike macke
                        state.cat.reference_image_id?.let { imageId ->
                            Image(
                                painter = rememberImagePainter(
                                    data = "https://cdn2.thecatapi.com/images/$imageId.jpg",
                                    builder = {
                                        crossfade(true)
                                    }
                                ),
                                contentDescription = "Cat image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .padding(bottom = 16.dp)
                                    .clip(shape = RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }


                        Text(buildBoldText("Description: ", cat.description), style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                onGalleryButtonClick(cat.id)
                            },
                            modifier = Modifier.align(Alignment.Start)
                        ) {
                            Text(text = "Gallery")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(buildBoldText("Countries of origin: ", cat.origin), style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(buildBoldText("Life span: ", cat.life_span), style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(buildBoldText("Weight: ", cat.weight.metric), style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(buildBoldText("Rare: ", if (cat.rare == 1) "Rare" else "Common"), style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(buildBoldText("Temperament:"), style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        val temperaments = cat.temperament.split(", ")
                        Column {
                            temperaments.forEach { temperament ->
                                SuggestionChip(onClick = {}, label = { Text(text = temperament, color = Color.Black) }, modifier = Modifier.padding(end = 8.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(buildBoldText("Characteristics:"), style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        CharacteristicsProgress("Adaptability", cat.adaptability)
                        CharacteristicsProgress("Affection Level", cat.affection_level)
                        CharacteristicsProgress("Stranger Friendly", cat.stranger_friendly)
                        CharacteristicsProgress("Dog Friendly", cat.dog_friendly)
                        CharacteristicsProgress("Energy Level", cat.energy_level)
                        CharacteristicsProgress("Social Needs", cat.social_needs)
                        CharacteristicsProgress("Health Issues", cat.health_issues)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                cat.wikipedia_url?.let {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                                    context.startActivity(intent)
                                }
                            },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(text = "Read more on Wikipedia")
                        }
                    }
                }
            }
        }
    )
}


@Composable
fun CharacteristicsProgress(label: String, value: Int) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = Color.Black)
        LinearProgressIndicator(
            progress = value / 5f,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = Color(0xFF6200EE)
        )
    }
}

fun buildBoldText(boldText: String, normalText: String? = null): AnnotatedString {
    return buildAnnotatedString {
        append(boldText)
        addStyle(style = SpanStyle(fontWeight = FontWeight.Bold), start = 0, end = boldText.length)
        if (normalText != null) {
            append(normalText)
        }
    }
}

//class CatDetailsStateParameterProvider : PreviewParameterProvider<CatDetailsContract.CatDetailsState> {
//    override val values: Sequence<CatDetailsContract.CatDetailsState> = sequenceOf(
//        CatDetailsContract.CatDetailsState(
//            loading = false,
//            cat =
//                CatDetailsUiModel(
//                    id = "abob",
//                    name = "American Bobtail",
//                    alt_names = "",
//                    description = "American Bobtails are loving and incredibly intelligent cats possessing a distinctive wild appearance. They are extremely interactive cats that bond with their human family with great devotion.",
//                    temperament = "Intelligent, Interactive, Lively",
//                    origin = "United States",
//                    life_span = "11 - 15",
//                    adaptability = 5,
//                    affection_level = 4,
//                    stranger_friendly = 3,
//                    dog_friendly = 2,
//                    energy_level = 1,
//                    social_needs = 3,
//                    health_issues = 4,
//                    intelligence = 5,
//                    rare = 1,
//                    wikipedia_url = "https://en.wikipedia.org/wiki/American_Bobtail",
//                    reference_image_id = null,
//                    weight = "5 - 7"
//                ),
//        ),
//    )
//}
//
//@Preview
//@Composable
//private fun PreviewCatsList(
//    @PreviewParameter(CatDetailsStateParameterProvider::class) catDetailsState: CatDetailsContract.CatDetailsState,
//) {
//    CatDetailsScreen(
//        state = catDetailsState,
//        onClose = {},
//        context = LocalContext.current,
//        onGalleryButtonClick = {}
//    )
//}
