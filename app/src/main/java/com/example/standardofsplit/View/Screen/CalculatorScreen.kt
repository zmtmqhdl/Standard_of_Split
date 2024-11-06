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

@Composable
fun CalculatorScreen(
    calculator: Calculator
) {

    val isToggled by calculator.changeMode.observeAsState()
    val nameChangeDialog = remember { mutableStateOf(false) }
    val buttonName = remember { mutableStateOf("A") }

    if (isToggled == true && nameChangeDialog.value ) {
        Button_Name_Dialog(
            onDismiss = { nameChangeDialog.value = false },
            onConfirm = { newName -> buttonName.value = newName
            nameChangeDialog.value = false },
            name = buttonName.value
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
                        width = 2.dp,
                        color = Color(0xFFDCD0FF),
                        shape = RoundedCornerShape(10.dp)
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
                Square_Button(
                    content = "A",
                    onClick = {
                        if (isToggled == true) {
                            nameChangeDialog.value = true
                        }
                    })

                Square_Button(content = "B") {
                }
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
                Square_Button(content = "C") {
                }
                Square_Button(content = "D") {
                }
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
                Square_Button(content = "E") {
                }
                Square_Button(content = "F") {
                }
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
                Square_Button(content = "G") {
                }
                Square_Button(content = "H") {
                }
                Square_Button(content = "전체") {
                }
                Square_Button(content = "적용") {
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}