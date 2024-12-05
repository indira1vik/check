package com.example.project2_cse535.screens


import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project2_cse535.components.BottomBarNavFunction
import com.example.project2_cse535.components.PageTitleHeader
import com.example.project2_cse535.modules.ModeManager

@Composable
fun SettingsScreen(navController: NavController) {
    Scaffold(
        bottomBar = { BottomBarNavFunction(navController, 2) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PageTitleHeader("Settings")
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                RadioSelectDiff()
                SetLevelBtn()
            }

        }
    }
}

@Composable
fun RadioSelectDiff() {
    val radioOptions = listOf(
        "Easy",
        "Medium",
        "Hard"
    )
    val (selectedOption, setSelectedOption) = remember {
        mutableStateOf(ModeManager.diffModeOption)
    }
    Column {
        radioOptions.forEach { item ->
            Row(
                modifier = Modifier
                    .clickable {
                        setSelectedOption(item)
                        ModeManager.diffModeOption = item
                    },
                verticalAlignment = Alignment.CenterVertically,

                ) {
                RadioButton(
                    selected = selectedOption == item,
                    onClick = {
                        setSelectedOption(item)
                        ModeManager.diffModeOption = item
                    }
                )
                Text(
                    text = item,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(0.dp, 8.dp, 8.dp, 8.dp)
                )
            }
        }
    }
}

@Composable
fun SetLevelBtn() {
    val context = LocalContext.current
    Button(
        modifier = Modifier.padding(24.dp),
        onClick = {
            Toast.makeText(context, "Mode: ${ModeManager.diffModeOption}", Toast.LENGTH_SHORT)
                .show()
        }
    ) {
        Text(text = "Set Level")
    }
}

@Preview
@Composable
fun TicPreviewThree() {
    SettingsScreen(navController = rememberNavController())
}