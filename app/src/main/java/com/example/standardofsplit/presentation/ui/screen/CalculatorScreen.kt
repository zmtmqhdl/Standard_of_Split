package com.example.standardofsplit.presentation.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standardofsplit.data.model.ReceiptClass
import com.example.standardofsplit.presentation.ui.component.Button_Name_Dialog
import com.example.standardofsplit.presentation.ui.component.FunctionButton
import com.example.standardofsplit.presentation.ui.component.CalculateButton
import com.example.standardofsplit.presentation.ui.component.PersonSelectButton
import com.example.standardofsplit.presentation.ui.component.NameChangeToggleButton
import com.example.standardofsplit.presentation.ui.component.formatNumberWithCommas
import com.example.standardofsplit.presentation.ui.component.showCustomToast
import com.example.standardofsplit.presentation.viewModel.Calculator
import com.example.standardofsplit.presentation.viewModel.Receipt
import com.example.standardofsplit.presentation.viewModel.Start
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CalculatorScreen(
    calculator: Calculator, start: Start, receipt: Receipt, onNext: () -> Unit, onBack: () -> Unit
) {

    val isToggled by calculator.changeMode.observeAsState()
    val nameChangeDialog = remember { mutableStateOf(false) }

    val ps by start.personCount.observeAsState(2)

    val buttonPermission by calculator.buttonPermissions.observeAsState(emptyMap())
    val buttonName by calculator.buttonNames.observeAsState(emptyMap())

    val receipts by receipt.receipts.observeAsState(emptyList<ReceiptClass>())

    val Key by calculator.Key.observeAsState(0)
    val KeyKey by calculator.KeyKey.observeAsState(0)
    var total by remember {
        mutableStateOf(
            formatNumberWithCommas((receipts[Key].ProductQuantity[KeyKey].toInt() * receipts[Key].ProductPrice[KeyKey].toInt()).toString())
        )
    }

    val context = LocalContext.current
    var isToastShowing by remember { mutableStateOf(false) }
    val showToast by calculator.showToastEvent.observeAsState()

    fun showToastIfNotShowing(message: String) {
        if (!isToastShowing) {
            isToastShowing = true
            showCustomToast(context, message)
            MainScope().launch {
                delay(2000)
                isToastShowing = false
            }
        }
    }

    BackHandler {
        onBack()
    }

    LaunchedEffect(ps, Key, KeyKey, showToast, receipts) {
        calculator.updateButtonPermissions(ps)
        calculator.updateButtonNamesBasedOnPermissions()
        
        if (receipts.isNotEmpty() && Key < receipts.size) {
            calculator.updateCurrentReceiptSize(receipts[Key].ProductPrice.size)
            
            if (Key > 0) {
                calculator.updatePreviousReceiptSize(receipts[Key - 1].ProductPrice.size)
            }
            
            if (KeyKey < receipts[Key].ProductPrice.size) {
                total = formatNumberWithCommas(
                    (receipts[Key].ProductQuantity[KeyKey].toInt() *
                            receipts[Key].ProductPrice[KeyKey].toInt()).toString()
                )
            }
        }
        
        if (showToast == true) {
            showToastIfNotShowing("되돌릴 항목이 존재하지 않습니다.")
            calculator._showToastEvent.value = false
        }
    }

    val payList = remember { mutableStateListOf<Int>() }

    val selectedIndex = remember { mutableIntStateOf(-1) }

    val buttonStates by calculator.buttonStates.observeAsState(emptyList())

    val isLastProduct = remember { mutableStateOf(false) }

    val isResetFromResult by calculator.isResetFromResult.observeAsState(false)

    LaunchedEffect(Key, KeyKey, isResetFromResult) {
        if (Key == 0 && KeyKey == 0 && isResetFromResult) {
            payList.clear()
            calculator.setChangeMode(false)
            calculator.setResetFromResult(false)
        }
    }

    if (nameChangeDialog.value) {
        val currentName = buttonName[selectedIndex.intValue.toString()] ?: ""

        Button_Name_Dialog(
            onDismiss = { nameChangeDialog.value = false },
            onConfirm = { index, newName ->
                selectedIndex.intValue = index
                calculator.updateButtonName(selectedIndex.intValue.toString(), newName)
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
                .height(300.dp).offset(y = 60.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = receipts[Key].PlaceName,
                    fontSize = 20.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = receipts[Key].ProductName[KeyKey],
                    fontSize = 48.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
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
                        fontSize = 40.sp,
                        color = Color.White
                    )
                }
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
                PersonSelectButton(
                    text = buttonName["1"] ?: "X",
                    flag = buttonStates[1],
                    onClick = {
                        if (isLastProduct.value) {
                            if (isToggled == true && buttonPermission["1"] == true) {
                                nameChangeDialog.value = true
                                selectedIndex.intValue = 1
                            } else {
                                showToastIfNotShowing("정산이 완료되었습니다. 정산을 확인해주세요.")
                            }
                        } else {
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
                        }
                    }
                )

                PersonSelectButton(
                    text = buttonName["2"] ?: "X",
                    flag = buttonStates[2],
                    onClick = {
                        if (isLastProduct.value) {
                            if (isToggled == true && buttonPermission["2"] == true) {
                                nameChangeDialog.value = true
                                selectedIndex.intValue = 2
                            } else {
                                showToastIfNotShowing("정산이 완료되었습니다. 정산을 확인해주세요.")
                            }
                        } else {
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
                        }
                    }
                )
                Box(
                    modifier = Modifier
                        .width(216.dp)
                        .height(105.dp),
                    contentAlignment = Alignment.Center
                ) {
                    NameChangeToggleButton(text1 = "OFF", text2 = "ON", onClick = {}, viewModel = calculator)
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PersonSelectButton(
                    text = buttonName["3"] ?: "X",
                    flag = buttonStates[3],
                    onClick = {
                        if (isLastProduct.value) {
                            if (isToggled == true && buttonPermission["3"] == true) {
                                nameChangeDialog.value = true
                                selectedIndex.intValue = 3
                            } else {
                                showToastIfNotShowing("정산이 완료되었습니다. 정산을 확인해주세요.")
                            }
                        } else {
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
                        }
                    }
                )
                PersonSelectButton(
                    text = buttonName["4"] ?: "X",
                    flag = buttonStates[4],
                    onClick = {
                        if (isLastProduct.value) {
                            if (isToggled == true && buttonPermission["4"] == true) {
                                nameChangeDialog.value = true
                                selectedIndex.intValue = 4
                            } else {
                                showToastIfNotShowing("정산이 완료되었습니다. 정산을 확인해주세요.")
                            }
                        } else {
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
                        }
                    }
                )
                Box(
                    modifier = Modifier
                        .width(216.dp)
                        .height(105.dp),
                    contentAlignment = Alignment.Center
                ) {
                    FunctionButton(text = "되돌리기", onClick = {
                        if (isLastProduct.value) {
                            isLastProduct.value = false
                            calculator.reDo()
                            if (receipts.isNotEmpty()) {
                                calculator.setKey(receipts.size - 1)
                                calculator.setKeyKey(receipts.last().ProductPrice.size - 1)
                                total = formatNumberWithCommas(
                                    (receipts[Key].ProductQuantity[KeyKey].toInt() *
                                     receipts[Key].ProductPrice[KeyKey].toInt()).toString()
                                )
                            }
                        } else {
                            calculator.reDo()
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
                PersonSelectButton(
                    text = buttonName["5"] ?: "X",
                    flag = buttonStates[5],
                    onClick = {
                        if (isLastProduct.value) {
                            if (isToggled == true && buttonPermission["5"] == true) {
                                nameChangeDialog.value = true
                                selectedIndex.intValue = 5
                            } else {
                                showToastIfNotShowing("정산이 완료되었습니다. 정산을 확인해주세요.")
                            }
                        } else {
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
                        }
                    }
                )
                PersonSelectButton(
                    text = buttonName["6"] ?: "X",
                    flag = buttonStates[6],
                    onClick = {
                        if (isLastProduct.value) {
                            if (isToggled == true && buttonPermission["6"] == true) {
                                nameChangeDialog.value = true
                                selectedIndex.intValue = 6
                            } else {
                                showToastIfNotShowing("정산이 완료되었습니다. 정산을 확인해주세요.")
                            }
                        } else {
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
                        }
                    }
                )
                Box(
                    modifier = Modifier
                        .width(216.dp)
                        .height(105.dp),
                    contentAlignment = Alignment.Center
                ) {
                    FunctionButton(
                        text = "전체 선택",
                        onClick = {
                            if (isLastProduct.value) {
                                showToastIfNotShowing("정산이 완료되었습니다. 정산을 확인해주세요.")
                            } else {
                                for (i in 1..8) {
                                    if (i <= ps) {
                                        payList.add(i)
                                        if (!buttonStates[i]) {
                                            calculator.toggleButtonState(i)
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PersonSelectButton(
                    text = buttonName["7"] ?: "X",
                    flag = buttonStates[7],
                    onClick = {
                        if (isLastProduct.value) {
                            if (isToggled == true && buttonPermission["7"] == true) {
                                nameChangeDialog.value = true
                                selectedIndex.intValue = 7
                            } else {
                                showToastIfNotShowing("정산이 완료되었습니다. 정산을 확인해주세요.")
                            }
                        } else {
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
                        }
                    }
                )
                PersonSelectButton(
                    text = buttonName["8"] ?: "X",
                    flag = buttonStates[8],
                    onClick = {
                        if (isLastProduct.value) {
                            if (isToggled == true && buttonPermission["8"] == true) {
                                nameChangeDialog.value = true
                                selectedIndex.intValue = 8
                            } else {
                                showToastIfNotShowing("정산이 완료되었습니다. 정산을 확인해주세요.")
                            }
                        } else {
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
                        }
                    }
                )

                CalculateButton(
                    text = if (isLastProduct.value) "정산 확인" else "적용",
                    onClick = {
                        if (isLastProduct.value) {
                            onNext()
                        } else {
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
                                    isLastProduct.value = true
                                    showCustomToast(context, "정산이 완료되었습니다. 정산을 확인해주세요.")
                                } else {
                                    calculator.incrementKeyKey()
                                    if (KeyKey >= receipts[Key].ProductPrice.size) {
                                        calculator.resetKeyKey()
                                        calculator.incrementKey()
                                    }
                                    total = formatNumberWithCommas(
                                        (receipts[Key].ProductQuantity[KeyKey].toInt() * receipts[Key].ProductPrice[KeyKey].toInt()).toString())
                                }
                            } else {
                                showToastIfNotShowing("최소 1명 이상을 선택해주세요.")
                            }
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}