package com.example.catlistapp.cats.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

import com.example.catlistapp.cats.list.model.CatListUiModel
import com.example.catlistapp.R

fun NavGraphBuilder.cats(
    route: String,
    onCatClick: (String) -> Unit,
) = composable(
    route = route
) {
    val catListViewModel = viewModel<CatListViewModel>()

    val state = catListViewModel.state.collectAsState()

    CatListScreen(
        state = state.value,
        eventPublisher = {
            catListViewModel.setEvent(it)
        },
        onCatClick = onCatClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatListScreen(
    state: CatListContract.CatListState,
    eventPublisher: (uiEvent: CatListContract.CatListUiEvent) -> Unit,
    onCatClick: (String) -> Unit,
) {

    var searchText by remember{ mutableStateOf("")}

    if(searchText.isEmpty()){
        eventPublisher(CatListContract.CatListUiEvent.CloseSearchMode)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.list_title),
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF6200EE)
                )
            )
        },
    ) { paddingValues ->
        if (state.loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .background(Color(0xFFF0E7FF))
            ) {

                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        if (it.isEmpty()) {
                            eventPublisher(CatListContract.CatListUiEvent.CloseSearchMode)
                        } else {
                            eventPublisher(CatListContract.CatListUiEvent.SearchQueryChanged(it))
                        }
                    },
                    label = { Text("Search cats") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search icon",
                            tint = Color.Gray
                        )
                    },
                    textStyle = TextStyle(color = Color.Black),
                )

                if(state.isSearchMode){

                    state.filteredCats.forEach { cat ->
                        CatCard(cat = cat, onCatClick = onCatClick)
                    }
                }
                else {
                    state.cats.forEach { cat ->
                        CatCard(cat = cat, onCatClick = onCatClick)
                    }
                }
            }
        }
    }
}

@Composable
fun CatCard(
    cat: CatListUiModel,
    onCatClick: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(bottom = 16.dp)
            .clickable { onCatClick(cat.id) },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = cat.name,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )

            Text(
                text = cat.description.take(250),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            val temperaments = cat.temperament.split(", ").take(3)
            Row(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                temperaments.forEach { temperament ->
                    SuggestionChip(
                        onClick = {},
                        label = { Text(text = temperament, color = Color.Black) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }
    }
}

class CatListStateParameterProvider : PreviewParameterProvider<CatListContract.CatListState> {
    override val values: Sequence<CatListContract.CatListState> = sequenceOf(
        CatListContract.CatListState(
            loading = false,
            cats = listOf(
                CatListUiModel(
                    id = "abob",
                    name = "American Bobtail",
                    alt_names = "",
                    description = "American Bobtails are loving and incredibly intelligent cats possessing a distinctive wild appearance. They are extremely interactive cats that bond with their human family with great devotion.",
                    temperament = "Intelligent, Interactive, Lively"
                ),
                CatListUiModel(
                    id = "mau",
                    name = "American Bobtail",
                    alt_names = "",
                    description = "American Bobtails are loving and incredibly intelligent cats possessing a distinctive wild appearance. They are extremely interactive cats that bond with their human family with great devotion.",
                    temperament = "Intelligent, Interactive, Lively"
                ),
                CatListUiModel(
                    id = "tau",
                    name = "American Bobtail",
                    alt_names = "",
                    description = "American Bobtails are loving and incredibly intelligent cats possessing a distinctive wild appearance. They are extremely interactive cats that bond with their human family with great devotion.",
                    temperament = "Intelligent, Interactive, Lively"
                ),
            ),
        ),
    )
}

@Preview
@Composable
private fun PreviewCatsList(
    @PreviewParameter(CatListStateParameterProvider::class) catListState: CatListContract.CatListState,
) {
    CatListScreen(
        state = catListState,
        eventPublisher = {},
        onCatClick = {},
    )
}
