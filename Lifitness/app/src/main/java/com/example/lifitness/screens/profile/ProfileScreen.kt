package com.example.lifitness.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import com.example.lifitness.common.composable.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lifitness.common.ext.spacer
import com.example.lifitness.ui.theme.BackgroundColor as background

@Composable
fun ProfileScreen(
    modifier: Modifier
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .background(background),
        horizontalAlignment = Alignment.Start
        
    ){
        Row {
            MoreTitle()
        }
        Spacer(modifier = Modifier.spacer())
        Box {
            ImageBackground()
            Row (horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()){
                ImageProfile()
                Icon(
                    Icons.Default.Create,
                    contentDescription = "Edit profile image",
                    tint = Color.White,
                    modifier = Modifier.padding(top = 10.dp).padding(end = 10.dp)
                )
            }

        }
        BioCard(
            nickname = "Pedro Marcelo",
            content = "My story begins with the Lifitness app, which has been my trusty companion on this fitness adventure. I believe that age is just a number, and with the right tools and mindset, we can all live life to the fullest.")
    }
}

@Preview
@Composable
fun PreviewProfileScreen(){
    ProfileScreen(modifier = Modifier)
}