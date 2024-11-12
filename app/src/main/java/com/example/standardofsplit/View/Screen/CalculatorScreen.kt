package com.example.standardofsplit.View.Screen

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standardofsplit.Model.ReceiptClass
import com.example.standardofsplit.View.Components.Button_Name_Dialog
import com.example.standardofsplit.View.Components.Rectangle_Button
import com.example.standardofsplit.View.Components.Square_Button
import com.example.standardofsplit.View.Components.Toggle_Square_Button
import com.example.standardofsplit.View.Components.Toggle_Name_Button
import com.example.standardofsplit.View.Components.formatNumberWithCommas
import com.example.standardofsplit.ViewModel.Calculator
import com.example.standardofsplit.ViewModel.Receipt
import com.example.standardofsplit.ViewModel.Start
import kotlin.math.absoluteValue

@Composable
fun CalculatorScreen(
    calculator: Calculator, start: Start, receipt: Receipt, onNext: () -> Unit
) {

    val isToggled by calculator.changeMode.observeAsState()
    val nameChangeDialog = remember { mutableStateOf(false) }

    val ps by start.personCount.observeAsState(2)

    val buttonPermission = remember { mutableStateMapOf<String, Boolean>() }
    val buttonName = remember { mutableStateMapOf<String, String>() }

    val receipts by receipt.receipts.observeAsState(emptyList<ReceiptClass>())

    LaunchedEffect(ps) {
        for (i in 1..8) {
            buttonName[i.toString()] = if (i <= ps) "인원$i" else "X"
            buttonPermission[i.toString()] = if (i <= ps) true else false
        }
    }

    val payList = remember { mutableStateListOf<Int>() }

    val selectedIndex = remember { mutableIntStateOf(-1) }

    val Key by calculator.Key.observeAsState(0)
    val KeyKey by calculator.KeyKey.observeAsState(0)
    var total by remember {
        mutableStateOf(
            formatNumberWithCommas((receipts[Key].ProductQuantity[KeyKey].toInt() * receipts[Key].ProductPrice[KeyKey].toInt()).toString())
        )
    }

    val stack by calculator.stack.observeAsState(emptyList())

    val buttonStates by calculator.buttonStates.observeAsState(emptyList())

    if (nameChangeDialog.value) {
        val currentName = buttonName[selectedIndex.intValue.toString()] ?: ""

        Button_Name_Dialog(
            onDismiss = { nameChangeDialog.value = false },
            onConfirm = { index, newName ->
                selectedIndex.intValue = index
                buttonName[selectedIndex.intValue.toString()] = newName // 이 부분 수정
                nameChangeDialog.value = false
            },
            name = currentName,
            index = selectedIndex.intValue
        )
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = "${receipts[Key].PlaceName} - ${receipts[Key].ProductName[KeyKey]}",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 20.sp
            )
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
                    text = "${total}원",
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
                Toggle_Square_Button(
                    content = buttonName["1"] ?: "X",
                    result = buttonStates[1],
                    onClick = {
                        if (isToggled == true && buttonPermission["1"] == true) {
                            nameChangeDialog.value = true
                            selectedIndex.intValue = 1
                        }
                        if (isToggled == false) {
                            calculator.toggleButtonState(1)
                            if (1 !in payList) {
                                payList.add(1)
                            } else {
                                payList.remove(1)
                            }
                        }
                    })

                Toggle_Square_Button(
                    content = buttonName["2"] ?: "X",
                    result = buttonStates[2],
                    onClick = {
                        if (isToggled == true && buttonPermission["2"] == true) {
                            nameChangeDialog.value = true
                            selectedIndex.intValue = 2
                        }
                        if (isToggled == false) {
                            calculator.toggleButtonState(2)
                            if (2 !in payList) {
                                payList.add(2)
                            } else {
                                payList.remove(2)
                            }
                        }
                    })
                Box(
                    modifier = Modifier
                        .width(216.dp)
                        .height(105.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Toggle_Name_Button(viewModel = calculator)
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Toggle_Square_Button(
                    content = buttonName["3"] ?: "X",
                    result = buttonStates[3],
                    onClick = {
                        if (isToggled == true && buttonPermission["3"] == true) {
                            nameChangeDialog.value = true
                            selectedIndex.intValue = 3
                        }
                        if (isToggled == false) {
                            calculator.toggleButtonState(3)
                            if (3 !in payList) {
                                payList.add(3)
                            } else {
                                payList.remove(3)
                            }
                        }
                    })
                Toggle_Square_Button(
                    content = buttonName["4"] ?: "X",
                    result = buttonStates[4],
                    onClick = {
                        if (isToggled == true && buttonPermission["4"] == true) {
                            nameChangeDialog.value = true
                            selectedIndex.intValue = 4
                        }
                        if (isToggled == false) {
                            calculator.toggleButtonState(4)
                            if (4 !in payList) {
                                payList.add(4)
                            } else {
                                payList.remove(4)
                            }
                        }
                    })
                Box(
                    modifier = Modifier
                        .width(216.dp)
                        .height(105.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Rectangle_Button(content = "되돌리기", onClick = {
                        calculator.decrementKeyKey()
                        if (Key != 0 && KeyKey != 0) {
                            calculator.decrementKeyKey()
                            if (KeyKey == -1) {
                                calculator.decrementKey()
                            }
                        } else {
                            // 0 0 이니까 더 없어요~
                        }
                    })
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Toggle_Square_Button(
                    content = buttonName["5"] ?: "X",
                    result = buttonStates[5],
                    onClick = {
                        if (isToggled == true && buttonPermission["5"] == true) {
                            nameChangeDialog.value = true
                            selectedIndex.intValue = 5
                        }
                        if (isToggled == false) {
                            calculator.toggleButtonState(5)
                            if (5 !in payList) {
                                payList.add(5)
                            } else {
                                payList.remove(5)
                            }
                        }
                    })
                Toggle_Square_Button(
                    content = buttonName["6"] ?: "X",
                    result = buttonStates[6],
                    onClick = {
                        if (isToggled == true && buttonPermission["6"] == true) {
                            nameChangeDialog.value = true
                            selectedIndex.intValue = 6
                        }
                        if (isToggled == false) {
                            calculator.toggleButtonState(6)
                            if (6 !in payList) {
                                payList.add(6)
                            } else {
                                payList.remove(6)
                            }
                        }
                    })
                Box(
                    modifier = Modifier
                        .width(216.dp)
                        .height(105.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Rectangle_Button(content = "전체 선택", onClick = {
                        for (i in 1..8) {
                            if (i <= ps) {
                                payList.add(i)
                                if (!buttonStates[i]) {
                                    calculator.toggleButtonState(i)
                                }
                            }
                        }
                    })
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Toggle_Square_Button(
                    content = buttonName["7"] ?: "X",
                    result = buttonStates[7],
                    onClick = {
                        if (isToggled == true && buttonPermission["7"] == true) {
                            nameChangeDialog.value = true
                            selectedIndex.intValue = 7
                        }
                        if (isToggled == false) {
                            calculator.toggleButtonState(7)
                            if (7 !in payList) {
                                payList.add(7)
                            } else {
                                payList.remove(7)
                            }
                        }
                    })
                Toggle_Square_Button(
                    content = buttonName["8"] ?: "X",
                    result = buttonStates[8],
                    onClick = {
                        if (isToggled == true && buttonPermission["8"] == true) {
                            nameChangeDialog.value = true
                            selectedIndex.intValue = 8
                        }
                        if (isToggled == false) {
                            calculator.toggleButtonState(8)
                            if (8 !in payList) {
                                payList.add(8)
                            } else {
                                payList.remove(8)
                            }
                        }
                    })

                Square_Button(content = "적용", onClick = {
                    if (payList.isNotEmpty()) {
                        calculator.resetButtonStates()
                        calculator.updatePersonPay(
                            payList,
                            receipts[Key].PlaceName,
                            receipts[Key].ProductName[KeyKey],
                            receipts[Key].ProductQuantity[KeyKey].toInt() * receipts[Key].ProductPrice[KeyKey].toInt()
                        )
                        payList.clear()
                        if (Key == receipts.size - 1 && KeyKey == receipts[Key].ProductPrice.size - 1) {
                            onNext()
                        } else {
                            calculator.incrementKeyKey()
                            if (receipts[Key].ProductPrice.size == KeyKey) {
                                calculator.resetKeyKey()
                                calculator.incrementKey()
                            }
                            total =
                                formatNumberWithCommas((receipts[Key].ProductQuantity[KeyKey].toInt() * receipts[Key].ProductPrice[KeyKey].toInt()).toString())
                        }

                    } else {
                        // 토스트메세ㅣ지
                    }
                })
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}