package com.example.catlistapp.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.catlistapp.R

fun NavGraphBuilder.login(
    route: String,
    onCreate: () -> Unit
) = composable(route = route) {
    val loginViewModel = hiltViewModel<LoginViewModel>()
    val state = loginViewModel.state.collectAsState()

    LoginScreen(
        state = state.value,
        eventPublisher = {
            loginViewModel.setEvent(it)
        },
        onCreate = onCreate
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    state: LogInContract.LoginState,
    eventPublisher: (uiEvent: LogInContract.LoginEvent) -> Unit,
    onCreate: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {},
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .padding(top = 60.dp)
                    .padding(horizontal = 10.dp)
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cat_logo),
                    contentDescription = "logo",
                    modifier = Modifier.padding(bottom = 5.dp)
                )

                Text(
                    text = "Welcome to Catapult",
                    modifier = Modifier
                        .padding(top = 20.dp),
                    style = TextStyle(
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    textAlign = TextAlign.Center
                )

                val textFieldColors = TextFieldDefaults.textFieldColors(
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                    containerColor = Color.Transparent,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary
                )


                TextField(
                    value = state.name,
                    onValueChange = { eventPublisher(LogInContract.LoginEvent.OnNameChange(it)) },
                    label = { Text("Name", color = MaterialTheme.colorScheme.onBackground) },
                    isError = !state.isNameValid,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    colors = textFieldColors
                )


                TextField(
                    value = state.nickname,
                    onValueChange = { eventPublisher(LogInContract.LoginEvent.OnNicknameChange(it)) },
                    label = { Text("Nickname", color = MaterialTheme.colorScheme.onBackground) },
                    isError = !state.isNicknameValid,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    colors = textFieldColors
                )


                TextField(
                    value = state.email,
                    onValueChange = { eventPublisher(LogInContract.LoginEvent.OnEmailChange(it)) },
                    label = { Text("Email", color = MaterialTheme.colorScheme.onBackground) },
                    isError = !state.isEmailValid,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    colors = textFieldColors
                )


                Spacer(modifier = Modifier.height(16.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        eventPublisher(LogInContract.LoginEvent.OnCreateProfile)
                        onCreate()
                    },
                    enabled = state.isNameValid && state.isNicknameValid && state.isEmailValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Create",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                }
            }
        }
    )
}


