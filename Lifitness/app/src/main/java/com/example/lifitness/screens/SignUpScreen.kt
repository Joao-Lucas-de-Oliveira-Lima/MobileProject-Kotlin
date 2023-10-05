package com.example.lifitness.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lifitness.R
import com.example.lifitness.components.BrandLogoName
import com.example.lifitness.components.LogoComponent
import com.example.lifitness.components.NormalTextComponent
import com.example.lifitness.components.TextFieldComponent

@Composable
fun SignUpScreen(){
    Box(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(top = 50.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ){
                LogoComponent()
                BrandLogoName(value = stringResource(id = R.string.brand_logo_name))
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 100.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ){
                TextFieldComponent(labelValue = stringResource(id = R.string.sign_up_username_field))
                TextFieldComponent(labelValue = stringResource(id = R.string.sign_up_email_field))
                TextFieldComponent(labelValue = stringResource(id = R.string.sign_up_password_field))
                TextFieldComponent(labelValue = stringResource(id = R.string.sign_up_retype_your_password_field))
            }

        }
    }
}

@Preview
@Composable
fun defaultPreviewSignUpScreen(){
    SignUpScreen()
}