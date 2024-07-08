package com.example.catlistapp.cats.list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

import com.example.catlistapp.R
import com.example.catlistapp.cats.api.model.CatApiModel
import com.example.catlistapp.cats.gallery.AppIconButton
import kotlinx.coroutines.launch

fun NavGraphBuilder.cats(
    route: String,
    onCatClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    onQuizClick: () -> Unit,
    onLeaderboardClick: (Int) -> Unit
) = composable(
    route = route
) {
    val catListViewModel: CatListViewModel = hiltViewModel()

    val state = catListViewModel.state.collectAsState()

    val uiScope = rememberCoroutineScope()
    val drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed)

    BackHandler(enabled = drawerState.isOpen){
        uiScope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        modifier = Modifier,
        drawerState = drawerState,
        drawerContent = {
            CatListDrawer(
                onProfileClick = {
                    uiScope.launch {
                        drawerState.close()
                    }
                    onProfileClick()
                },
                onQuizClick = {
                    uiScope.launch {
                        drawerState.close()
                    }
                    onQuizClick()
                },
                onLeaderboardClick = {
                    uiScope.launch {
                        drawerState.close()
                    }
                    onLeaderboardClick(it)
                }
            )
        },
        content = {

            CatListScreen(
                state = state.value,
                eventPublisher = {
                    catListViewModel.setEvent(it)
                },
                onCatClick = onCatClick,
                onDrawerMenuClick = {
                    uiScope.launch {
                        drawerState.open()
                    }
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatListScreen(
    state: CatListContract.CatListState,
    eventPublisher: (uiEvent: CatListContract.CatListUiEvent) -> Unit,
    onCatClick: (String) -> Unit,
    onDrawerMenuClick: () -> Unit,
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
                        ),
                        navigationIcon = {
                            AppIconButton(
                                imageVector = Icons.Default.Menu,
                                onClick = onDrawerMenuClick,
                            )
                        }
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
                                    eventPublisher(
                                        CatListContract.CatListUiEvent.SearchQueryChanged(
                                            it
                                        )
                                    )
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

                        if (state.isSearchMode) {

                            state.filteredCats.forEach { cat ->
                                CatCard(cat = cat, onCatClick = onCatClick)
                            }
                        } else {
                            state.cats.forEach { cat ->
                                CatCard(cat = cat, onCatClick = onCatClick)
                            }
                        }
                    }
                }
            }
}

@Composable
private fun CatListDrawer(
    onProfileClick: () -> Unit,
    onQuizClick: () -> Unit,
    onLeaderboardClick: (Int) -> Unit,
){
    BoxWithConstraints {
        ModalDrawerSheet (
            modifier = Modifier.width(maxWidth * 3 / 4),
            drawerContainerColor = MaterialTheme.colorScheme.background,
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.BottomStart
                ){
                    Text(
                        modifier = Modifier
                            .padding(top = 90.dp)
                            .align(Alignment.Center)
                            .padding(horizontal = 16.dp),
                        text = "Catapult",
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    )
                    Image(
                        painter = painterResource(id = R.drawable.cat_logo),
                        contentDescription = "logo",
                        modifier = Modifier.align(Alignment.Center)
                    )
                    HorizontalDivider(
                        modifier = Modifier
//                            .padding(bottom = 5.dp)
                            .fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Column(modifier = Modifier.weight(1f)){

                    AppDrawerActionItem(
                        icon = Icons.Default.Person,
                        text = "Profile",
                        onClick = onProfileClick,
                        color = MaterialTheme.colorScheme.background
                    )

                    NavigationDrawerItem(
                        label = {
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                text = "Catalog"
                            )
                        },
                        icon = {
                            Icon(imageVector = Icons.Default.MenuBook, contentDescription = null)
                        },
                        selected = true,
                        onClick = {

                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = Color.White,
                            selectedIconColor = Color.White
                        )
                    )

                    AppDrawerActionItem(
                        icon = Icons.Default.Quiz,
                        text = "Quiz",
                        onClick = onQuizClick,
                        color = MaterialTheme.colorScheme.background
                    )

                    AppDrawerActionItem(
                        icon = Icons.Default.Leaderboard,
                        text = "Leaderboard",
                        onClick = {onLeaderboardClick(1)},
                        color = MaterialTheme.colorScheme.background
                    )

                    HorizontalDivider(
                        modifier = Modifier
                            .padding(bottom = 5.dp)
                            .fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

        }
    }
}

@Composable
fun AppDrawerActionItem(
    icon: ImageVector,
    text: String,
    color: Color,
    onClick: (() -> Unit)? = null
){
    ListItem(
        modifier = Modifier.clickable(
            enabled = onClick != null,
            onClick = { onClick?.invoke() }
        ),
        leadingContent = {
            Icon(imageVector = icon, contentDescription = null)
        },
        headlineContent = {
            Text(text = text)
        },
        colors = ListItemDefaults.colors(
            containerColor = color
        )
    )
}

@Composable
fun CatCard(
    cat: CatApiModel,
    onCatClick: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(bottom = 16.dp)
            .clickable {
                onCatClick(cat.id)
                println("Kliknuto na macku sa id" + cat.id)
            },
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


