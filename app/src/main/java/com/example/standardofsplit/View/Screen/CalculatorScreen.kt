package com.example.standardofsplit.View.Screen

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standardofsplit.View.Components.Basic_Button
import com.example.standardofsplit.View.Components.Button_Name_Dialog
import com.example.standardofsplit.View.Components.Square_Button
import com.example.standardofsplit.View.Components.Toggle_Button
import com.example.standardofsplit.ViewModel.Calculator
import com.example.standardofsplit.ViewModel.Start

@Composable
fun CalculatorScreen(
    calculator: Calculator,
    start: Start
) {

    val isToggled by calculator.changeMode.observeAsState()
    val nameChangeDialog = remember { mutableStateOf(false) }


    val ps by start.personCount.observeAsState()

    val buttonName = remember { mutableStateMapOf<String, String>() }
    for (i in 1..8) {
        buttonName[i.toString()] = if (i <= ps) "인원$i" else "X"
    }

    val selectedIndex = remember { mutableStateOf(-1) }

    if (nameChangeDialog.value) {
        val currentName = buttonName[selectedIndex.value.toString()] ?: "기본값"

        Button_Name_Dialog(
            onDismiss = { nameChangeDialog.value = false },
            onConfirm = { index, newName ->
                selectedIndex.value = index
                buttonName[selectedIndex.value.toString()] = newName // 이 부분 수정
                nameChangeDialog.value = false
            },
            name = currentName,
            index = selectedIndex.value
        )
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .border(
                        width = 2.dp, color = Color(0xFFDCD0FF), shape = RoundedCornerShape(10.dp)
                    )
                    .clip(RoundedCornerShape(10.dp))
                    .padding(16.dp)
                    .height(50.dp)
                    .width(350.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "원",
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 40.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Square_Button(content = buttonName["1"] ?: "X", onClick = {
                    if (isToggled == true) {
                        nameChangeDialog.value = true
                        selectedIndex.value = 1
                    }
                })

                Square_Button(content = buttonName["2"] ?: "X", onClick = {
                    if (isToggled == true) {
                        nameChangeDialog.value = true
                        selectedIndex.value = 2
                    }
                })
                Box(
                    modifier = Modifier
                        .width(216.dp)
                        .height(105.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Toggle_Button(viewModel = calculator)
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Square_Button(content = buttonName["3"] ?: "X", onClick = {
                    if (isToggled == true) {
                        nameChangeDialog.value = true
                        selectedIndex.value = 3
                    }
                })
                Square_Button(content = buttonName["4"] ?: "X", onClick = {
                    if (isToggled == true) {
                        nameChangeDialog.value = true
                        selectedIndex.value = 4
                    }
                })
                Box(
                    modifier = Modifier
                        .width(216.dp)
                        .height(105.dp)
                ) {}
            }

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Square_Button(content = buttonName["5"] ?: "X", onClick = {
                    if (isToggled == true) {
                        nameChangeDialog.value = true
                        selectedIndex.value = 5
                    }
                })
                Square_Button(content = buttonName["6"] ?: "X", onClick = {
                    if (isToggled == true) {
                        nameChangeDialog.value = true
                        selectedIndex.value = 6
                    }
                })
                Box(
                    modifier = Modifier
                        .width(216.dp)
                        .height(105.dp)
                ) {}
            }

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Square_Button(content = buttonName["7"] ?: "X", onClick = {
                    if (isToggled == true) {
                        nameChangeDialog.value = true
                        selectedIndex.value = 7
                    }
                })
                Square_Button(content = buttonName["8"] ?: "X", onClick = {
                    if (isToggled == true) {
                        nameChangeDialog.value = true
                        selectedIndex.value = 8
                    }
                })
                Square_Button(content = "전체") {}
                Square_Button(content = "적용") {}
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}