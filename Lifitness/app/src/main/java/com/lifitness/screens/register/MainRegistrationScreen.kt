package com.lifitness.screens.register

import ButtonWithoutIconComposable
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lifitness.R
import com.lifitness.app.LifitnessScreen
import com.lifitness.common.composable.ClickableLoginTextComponent
import com.lifitness.common.composable.ClickablePolicyTextAndTermsTextComponent
import com.lifitness.common.composable.DividerTextComposable
import com.lifitness.common.composable.FacebookLoginButton
import com.lifitness.common.composable.GoogleLoginButton
import com.lifitness.common.composable.LogoComponent
import com.lifitness.common.composable.NormalTextComposable
import com.lifitness.common.composable.RegistrationProgressBarComposable
import com.lifitness.singleton.LoggedInUserSingleton
import com.stevdzasan.onetap.OneTapSignInWithGoogle
import com.stevdzasan.onetap.rememberOneTapSignInState


@Composable
fun MainRegistrationScreen(navController: NavHostController) {
    var isPasswordVisible = remember {
        mutableStateOf(false)
    }
    val visualTransformation = if (isPasswordVisible.value) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }

    var isNavigationDone by remember { mutableStateOf(false) }
    val userSingleton = LoggedInUserSingleton.getInstance()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(colorResource(id = R.color.screen_background_color)),
            //.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val viewModel = viewModel<MainRegistrationScreenViewModel>()
        val state = viewModel.state
        val context = LocalContext.current
        LaunchedEffect(key1 = context) {
            viewModel.validationEvents.collect { event ->
                when (event) {
                    is MainRegistrationScreenViewModel.ValidationEvent.Success -> {
                        viewModel.createUser(context)
                    }
                }
            }
        }
        if (!state.isLoading && state.isSuccessLogin && !isNavigationDone) {
            navController.navigate(LifitnessScreen.PersonalData.name)
            isNavigationDone = true
        }
        val googleAuth = rememberOneTapSignInState()
        OneTapSignInWithGoogle(
            state = googleAuth,
            clientId = stringResource(id = R.string.web_client_Id),
            onTokenIdReceived = { tokenId ->
                viewModel.authenticateGoogleWithFirebase(context, tokenId)
                if (!state.isLoading && !isNavigationDone) {
                    if (userSingleton.age == 0) {
                        navController.navigate(LifitnessScreen.PersonalData.name)
                    } else {
                        navController.navigate(LifitnessScreen.Home.name)
                    }
                    isNavigationDone = true
                }
                Log.d("Token", tokenId)
            },
            onDialogDismissed = { message ->
                Log.d("Token", message)
            }
        )
        Box(
            //modifier = Modifier.align(Alignment.Center)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxSize(),
            ) {
                Spacer(modifier = Modifier.height(15.dp))
                RegistrationProgressBarComposable(
                    currentStep = 1,
                    totalSteps = 5,
                    Color(255, 102, 102),
                    240
                )
                Spacer(modifier = Modifier.height(15.dp))
                LogoComponent(50, 60)
                Spacer(modifier = Modifier.height(3.dp))
                NormalTextComposable(
                    text = stringResource(id = R.string.greeting_text),
                    fontSize = 15,
                    color = Color.White,
                )
                NormalTextComposable(
                    text = stringResource(id = R.string.create_an_account_text),
                    fontSize = 25,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(3.dp))
                //Username Field
                OutlinedTextField(
                    value = state.username,
                    onValueChange = {
                        viewModel.onEvent(MainRegistrationScreenFormEvent.UsernameChanged(it))
                    },
                    isError = state.usernameError != null,
                    modifier = Modifier
                        .background(Color(35, 33, 33), RoundedCornerShape(12.dp)),
                    label = {
                        Text(
                            text = stringResource(id = R.string.sign_up_username_field),
                            color = Color.White
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedLabelColor = Color.White,
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    textStyle = TextStyle(color = Color.White),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                )
                if (state.usernameError != null) {
                    Text(
                        text = state.usernameError,
                        fontSize = 14.sp,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .padding(horizontal = 50.dp),
                        textAlign = TextAlign.End
                    )
                }
                //
                Spacer(modifier = Modifier.height(3.dp))
                //Email field
                OutlinedTextField(
                    value = state.email,
                    onValueChange = {
                        viewModel.onEvent(MainRegistrationScreenFormEvent.EmailChanged(it))
                    },
                    isError = state.emailError != null,
                    modifier = Modifier
                        .background(Color(35, 33, 33), RoundedCornerShape(12.dp)),
                    label = {
                        Text(
                            text = stringResource(id = R.string.sign_up_email_field),
                            color = Color.White
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedLabelColor = Color.White,
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    textStyle = TextStyle(color = Color.White),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                )
                if (state.emailError != null) {
                    Text(
                        text = state.emailError,
                        fontSize = 14.sp,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .padding(horizontal = 50.dp),
                        textAlign = TextAlign.End
                    )
                }
                //
                Spacer(modifier = Modifier.height(3.dp))
                //Password Field
                OutlinedTextField(
                    value = state.password,
                    onValueChange = {
                        viewModel.onEvent(MainRegistrationScreenFormEvent.PasswordChanged(it))
                    },
                    isError = state.passwordError != null,
                    modifier = Modifier
                        .background(Color(35, 33, 33), RoundedCornerShape(12.dp)),
                    label = {
                        Text(
                            text = stringResource(id = R.string.sign_up_password_field),
                            color = Color.White
                        )
                    },
                    visualTransformation = visualTransformation,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedLabelColor = Color.White,
                    ),
                    textStyle = TextStyle(color = Color.White),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = "",
                            tint = Color.White
                        )
                    },
                    trailingIcon = {
                        val iconImage = if (isPasswordVisible.value) {
                            Icons.Default.Visibility
                        } else {
                            Icons.Default.VisibilityOff
                        }
                        IconButton(onClick = {
                            isPasswordVisible.value = !isPasswordVisible.value
                        }) {
                            Icon(
                                iconImage,
                                contentDescription = "",
                                tint = Color.White
                            )
                        }
                    }
                )
                if (state.passwordError != null) {
                    Text(
                        text = state.passwordError,
                        fontSize = 14.sp,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .padding(horizontal = 50.dp),
                        textAlign = TextAlign.End
                    )
                }
                //
                Spacer(modifier = Modifier.height(3.dp))
                //
                //Password Field
                OutlinedTextField(
                    value = state.repeatedPassword,
                    onValueChange = {
                        viewModel.onEvent(MainRegistrationScreenFormEvent.RepeatedPasswordChanged(it))
                    },
                    isError = state.repeatedPasswordError != null,
                    modifier = Modifier
                        .background(Color(35, 33, 33), RoundedCornerShape(12.dp)),
                    label = {
                        Text(
                            text = stringResource(id = R.string.sign_up_retype_your_password_field),
                            color = Color.White
                        )
                    },
                    visualTransformation = visualTransformation,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedLabelColor = Color.White,
                    ),
                    textStyle = TextStyle(color = Color.White),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = "",
                            tint = Color.White
                        )
                    },
                    trailingIcon = {
                        val iconImage = if (isPasswordVisible.value) {
                            Icons.Default.Visibility
                        } else {
                            Icons.Default.VisibilityOff
                        }
                        IconButton(onClick = {
                            isPasswordVisible.value = !isPasswordVisible.value
                        }) {
                            Icon(
                                iconImage,
                                contentDescription = "",
                                tint = Color.White
                            )
                        }
                    }
                )
                if (state.repeatedPasswordError != null) {
                    Text(
                        text = state.repeatedPasswordError,
                        fontSize = 14.sp,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .padding(horizontal = 50.dp),
                        textAlign = TextAlign.End
                    )
                }
                //
                Spacer(modifier = Modifier.height(5.dp))
                //CheckboxComposable(text = stringResource(id = R.string.privacy_policy_and_terms_of_use_text))
                //CheckBox
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 10.dp)
                        .padding(horizontal = 34.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = state.acceptedTerms,
                        onCheckedChange = {
                            viewModel.onEvent(MainRegistrationScreenFormEvent.AcceptTerms(it))
                        }
                    )
                    ClickablePolicyTextAndTermsTextComponent(
                        stringResource(id = R.string.privacy_policy_and_terms_of_use_text),
                        navController
                    )
                }
                if (state.termsError != null) {
                    Text(
                        text = state.termsError,
                        fontSize = 14.sp,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .padding(horizontal = 50.dp),

                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                ButtonWithoutIconComposable(
                    text = stringResource(id = R.string.next_title),
                    textColor = Color.Black,
                    fontSize = 17,
                    minHeight = 30,
                    buttonColor = Color.White,
                    horizontalPadding = 50,
                    onClick = {
                        viewModel.onEvent(MainRegistrationScreenFormEvent.Submit)
                    }
                )
                Spacer(modifier = Modifier.height(2.dp))
                DividerTextComposable(
                    text = stringResource(id = R.string.divisive_text),
                    fontSize = 18,
                    color = Color.White,
                    thickness = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    modifier = Modifier.width(230.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    GoogleLoginButton(
                        23,
                        Color.White
                    ) { googleAuth.open() }
                    ClickableLoginTextComponent(
                        text = stringResource(id = R.string.create_an_account_text)
                    ) { navController.navigate(LifitnessScreen.Login.name) }
                }
                //
                Spacer(modifier = Modifier.height(2.dp))
                //ClickableLoginTextComponent


                Spacer(modifier = Modifier.height(15.dp))
                if (state.isLoading){
                    CircularProgressIndicator(
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewMainRegistrationScreen() {
    val navController = rememberNavController()
    MainRegistrationScreen(navController)
}