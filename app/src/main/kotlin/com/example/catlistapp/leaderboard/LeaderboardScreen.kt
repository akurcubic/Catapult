package com.example.catlistapp.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.catlistapp.cats.gallery.AppIconButton

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.leaderboardScreen(
    route: String,
    navController: NavController,
    arguments: List<NamedNavArgument>,
) = composable(route = route, arguments = arguments) {
    val leaderboardViewModel: LeaderboardViewModel = hiltViewModel()
    val state by leaderboardViewModel.leaderboardState.collectAsState()

    Surface(
        tonalElevation = 1.dp
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Leaderboard",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    },
                    navigationIcon = {
                        AppIconButton(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            onClick = {
                                navController.navigate("cats")
                            }
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                )
            },
            content = {
                LeaderboardScreen(
                    state = state,
                    paddingValues = it,
                )
            }
        )
    }
}

@Composable
fun LeaderboardScreen(
    state: LeaderboardContract.LeaderboardState,
    paddingValues: PaddingValues,
) {
    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (state.error != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            val errorMessage = when (state.error) {
                is LeaderboardContract.LeaderboardState.DetailsError.DataUpdateFailed ->
                    "Failed to load. Error message: ${state.error.cause?.message}."
            }

            Text(
                text = errorMessage,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            itemsIndexed(
                items = state.results,
            ) { index, user ->
                val repeats = state.results.count { it.nickname == user.nickname }
                ListItem(
                    colors = if (state.nick == user.nickname)
                        ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.background)
                    else
                        ListItemDefaults.colors(),
                    headlineContent = {
                        Text(
                            text = user.nickname,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    },
                    supportingContent = {
                        Text(
                            "The number of quizzes played: $repeats",
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    },
                    trailingContent = {
                        Text(
                            "${user.result} points",
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    },
                    leadingContent = {
                        Text(
                            text = (index + 1).toString(),
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                )
                HorizontalDivider()
            }
        }
    }
}