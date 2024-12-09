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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.standardofsplit.presentation.ui.component.Button_Name_Dialog
import com.example.standardofsplit.presentation.ui.component.FunctionButton
import com.example.standardofsplit.presentation.ui.component.CalculateButton
import com.example.standardofsplit.presentation.ui.component.PersonSelectButton
import com.example.standardofsplit.presentation.ui.component.formatNumberWithCommas
import com.example.standardofsplit.presentation.ui.component.showCustomToast
import com.example.standardofsplit.presentation.viewModel.CalculatorViewModel
import com.example.standardofsplit.presentation.viewModel.ReceiptViewModel
import com.example.standardofsplit.presentation.viewModel.StartViewModel

@Composable
fun CalculatorScreen(
    onNext: () -> Unit, onBack: () -> Unit
) {

    val startViewModel: StartViewModel = hiltViewModel()
    val receiptViewModel: ReceiptViewModel = hiltViewModel()
    val calculatorViewModel: CalculatorViewModel = hiltViewModel()

    val personCount by startViewModel.personCount.collectAsState()

    val receipts by receiptViewModel.receipts.collectAsState(emptyList())

    val receiptKey by calculatorViewModel.receiptKey.collectAsState(0)
    val productKey by calculatorViewModel.productKey.collectAsState(0)
    val buttonNames by calculatorViewModel.buttonNames.collectAsState(mutableListOf())
    val buttonStates by calculatorViewModel.buttonState.collectAsState(List(8) { false })
    val buttonPermissions by calculatorViewModel.buttonPermissions.collectAsState(List(8) { false})

    val context = LocalContext.current

    var total by remember {
        mutableStateOf(
            formatNumberWithCommas((receipts[receiptKey].productQuantity.value[productKey] * receipts[receiptKey].productPrice.value[productKey]).toString())
        )
    }

    BackHandler {
        onBack()
    }

    ///


    val nameChangeDialog = remember { mutableStateOf(false) }

    LaunchedEffect(personCount, receiptKey, productKey, showToast, receipts) {
        calculatorViewModel.initializeButtonNames()

        if (receipts.isNotEmpty() && receiptKey < receipts.size) {
            calculatorViewModel.updateCurrentReceiptSize(receipts[receiptKey].productPrice.size)

            if (receiptKey > 0) {
                calculatorViewModel.updatePreviousReceiptSize(receipts[receiptKey - 1].productPrice.size)
            }

            if (productKey < receipts[receiptKey].productPrice.size) {
                total = formatNumberWithCommas(
                    (receipts[receiptKey].productupdateTotalPayQuantity[productKey].toInt() * receipts[receiptKey].productPrice[productKey].toInt()).toString()
                )
            }
        }

        if (showToast == true) {
            showCustomToast(message = "되돌릴 항목이 존재하지 않습니다.", context = context)
        }
    }

    val selectedIndex = remember { mutableIntStateOf(-1) }

    val isLastProduct = remember { mutableStateOf(false) }

    val isResetFromResult by calculatorViewModel.isResetFromResult.observeAsState(false)

    LaunchedEffect(receiptKey, productKey, isResetFromResult) {
        if (receiptKey == 0 && productKey == 0 && isResetFromResult) {
            selectedPerson.clear()
            calculatorViewModel.setChangeMode(false)
            calculatorViewModel.setResetFromResult(false)
        }
    }

    if (nameChangeDialog.value) {
        val currentName = buttonNames[selectedIndex.intValue.toString()] ?: ""

        Button_Name_Dialog(onDismiss = { nameChangeDialog.value = false },
            onConfirm = { index, newName ->
                selectedIndex.intValue = index
                calculatorViewModel.updateButtonNames(selectedIndex.intValue.toString(), newName)
                nameChangeDialog.value = false
            },
            name = currentName,
            index = selectedIndex.intValue
        )
    }

    /// 여기서부터

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .offset(y = 60.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = receipts[receiptKey].placeName,
                    fontSize = 20.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                // text = receipts[receiptKey].productName.value[productKey],
                //연구
                Text(
                    text = receipts[receiptKey].productName.value[productKey],
                    fontSize = 48.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

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
                        .width(350.dp), contentAlignment = Alignment.Center
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
                    text = buttonNames[0],
                    state = buttonStates[0],
                    onClick = {
                        if (isLastProduct.value) {
                            if (changeMode == true && buttonPermissions[0] == true) {
                                nameChangeDialog.value = true
                                selectedIndex.intValue = 0
                            } else {
                                showToastIfNotShowing("정산이 완료되었습니다. 정산을 확인해주세요.")
                            }
                        } else {
                            if (changeMode == true && buttonPermissions[0] == true) {
                                nameChangeDialog.value = true
                                selectedIndex.intValue = 1
                            }
                            if (changeMode == false) {
                                calculatorViewModel.buttonPush(0)
                            }
                        }
                    })

                PersonSelectButton(text = buttonNames["2"] ?: "X",
                    state = buttonStates[2],
                    onClick = {
                        if (isLastProduct.value) {
                            if (changeMode == true && buttonPermission["2"] == true) {
                                nameChangeDialog.value = true
                                selectedIndex.intValue = 2
                            } else {
                                showCustomToast(message = "정산이 완료되었습니다. 정산을 확인해주세요.", context = context)
                            }
                        } else {
                            if (changeMode == true && buttonPermission["2"] == true) {
                                nameChangeDialog.value = true
                                selectedIndex.intValue = 2
                            }
                            if (changeMode == false) {
                                calculatorViewModel.buttonPush(2)
                            }
                        }
                    })
                Box(
                    modifier = Modifier
                        .width(216.dp)
                        .height(105.dp),
                    contentAlignment = Alignment.Center
                ) {
                    NameChangeToggleButton(
                        text1 = "OFF", text2 = "ON", onClick = {}, viewModel = calculatorViewModel
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
                PersonSelectButton(text = buttonNames["3"] ?: "X",
                    state = buttonStates[3],
                    onClick = {
                        if (isLastProduct.value) {
                            if (changeMode == true && buttonPermission["3"] == true) {
                                nameChangeDialog.value = true
                                selectedIndex.intValue = 3
                            } else {
                                showCustomToast(message = "정산이 완료되었습니다. 정산을 확인해주세요.", context = context)
                            }
                        } else {
                            if (changeMode == true && buttonPermission["3"] == true) {
                                nameChangeDialog.value = true
                                selectedIndex.intValue = 3
                            }
                            if (changeMode == false) {
                                calculatorViewModel.buttonPush(3)
                            }
                        }
                    })
                PersonSelectButton(text = buttonNames["4"] ?: "X",
                    state = buttonStates[4],
                    onClick = {
                        if (isLastProduct.value) {
                            if (changeMode == true && buttonPermission["4"] == true) {
                                nameChangeDialog.value = true
                                selectedIndex.intValue = 4
                            } else {
                                showCustomToast(message = "정산이 완료되었습니다. 정산을 확인해주세요.", context = context)
                            }
                        } else {
                            if (changeMode == true && buttonPermission["4"] == true) {
                                nameChangeDialog.value = true
                                selectedIndex.intValue = 4
                            }
                            if (changeMode == false) {
                                calculatorViewModel.buttonPush(4)
                            }
                        }
                    })
                Box(
                    modifier = Modifier
                        .width(216.dp)
                        .height(105.dp),
                    contentAlignment = Alignment.Center
                ) {
                    FunctionButton(text = "되돌리기", onClick = {
                        if (isLastProduct.value) {
                            isLastProduct.value = false
                            calculatorViewModel.rollback()
                            if (receipts.isNotEmpty()) {
                                calculatorViewModel.setReceiptKey(receipts.size - 1)
                                calculatorViewModel.setProductKey(receipts.last().productPrice.size - 1)
                                total = formatNumberWithCommas(
                                    (receipts[receiptKey].productQuantity[productKey].toInt() * receipts[receiptKey].productPrice[productKey].toInt()).toString()
                                )
                            }
                        } else {
                            calculatorViewModel.rollback()
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
                PersonSelectButton(text = buttonNames["5"] ?: "X",
                    state = buttonStates[5],
                    onClick = {
                        if (isLastProduct.value) {
                            if (changeMode.value == true && buttonPermission["5"] == true) {
                                nameChangeDialog.value = true
                                selectedIndex.intValue = 5
                            } else {
                                showToastIfNotShowing("정산이 완료되었습니다. 정산을 확인해주세요.")
                            }
                        } else {
                            if (changeMode.value == true && buttonPermission["5"] == true) {
                                nameChangeDialog.value = true
                                selectedIndex.intValue = 5
                            }
                            if (changeMode.value == false) {
                                calculatorViewModel.buttonPush(5)
                            }
                        }
                    })
                PersonSelectButton(text = buttonNames["6"] ?: "X",
                    state = buttonStates[6],
                    onClick = {
                        if (isLastProduct.value) {
                            if (changeMode.value == true && buttonPermission["6"] == true) {
                                nameChangeDialog.value = true
                                selectedIndex.intValue = 6
                            } else {
                                showToastIfNotShowing("정산이 완료되었습니다. 정산을 확인해주세요.")
                            }
                        } else {
                            if (changeMode.value == true && buttonPermission["6"] == true) {
                                nameChangeDialog.value = true
                                selectedIndex.intValue = 6
                            }
                            if (changeMode.value == false) {
                                calculatorViewModel.buttonPush(6)

                            }
                        }
                    })
                Box(
                    modifier = Modifier
                        .width(216.dp)
                        .height(105.dp),
                    contentAlignment = Alignment.Center
                ) {
                    FunctionButton(text = "전체 선택", onClick = {
                        if (isLastProduct.value) {
                            showToastIfNotShowing("정산이 완료되었습니다. 정산을 확인해주세요.")
                        } else {
                            for (i in 1..8) {
                                if (i <= personCount) {
                                    selectedPerson.add(i)
                                    if (!buttonStates[i]) {
                                        calculatorViewModel.buttonPush(i)
                                    }
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
                PersonSelectButton(text = buttonNames["7"] ?: "X",
                    state = buttonStates[7],
                    onClick = {
                        if (isLastProduct.value) {
                            if (changeMode == true && buttonPermission["7"] == true) {
                                nameChangeDialog.value = true
                                selectedIndex.intValue = 7
                            } else {
                                showToastIfNotShowing("정산이 완료되었습니다. 정산을 확인해주세요.")
                            }
                        } else {
                            if (changeMode == true && buttonPermission["7"] == true) {
                                nameChangeDialog.value = true
                                selectedIndex.intValue = 7
                            }
                            if (changeMode == false) {
                                calculatorViewModel.buttonPush(7)
                                if (7 !in selectedPerson) {
                                    selectedPerson.add(7)
                                } else {
                                    selectedPerson.remove(7)
                                }
                            }
                        }
                    })
                PersonSelectButton(text = buttonNames["8"] ?: "X",
                    state = buttonStates[8],
                    onClick = {
                        if (isLastProduct.value) {
                            if (changeMode == true && buttonPermission["8"] == true) {
                                nameChangeDialog.value = true
                                selectedIndex.intValue = 8
                            } else {
                                showToastIfNotShowing("정산이 완료되었습니다. 정산을 확인해주세요.")
                            }
                        } else {
                            if (changeMode == true && buttonPermission["8"] == true) {
                                nameChangeDialog.value = true
                                selectedIndex.intValue = 8
                            }
                            if (changeMode == false) {
                                calculatorViewModel.buttonPush(8)
                                if (8 !in selectedPerson) {
                                    selectedPerson.add(8)
                                } else {
                                    selectedPerson.remove(8)
                                }
                            }
                        }
                    })

                CalculateButton(text = if (isLastProduct.value) "정산 확인" else "적용", onClick = {
                    if (isLastProduct.value) {
                        onNext()
                    } else {
                        if (selectedPerson.isNotEmpty()) {
                            calculatorViewModel.resetButtonStates()
                            calculatorViewModel.updateTotalPay(
                                selectedPerson,
                                receipts[receiptKey].placeName,
                                receipts[receiptKey].productName[productKey],
                                receipts[receiptKey].productQuantity[productKey].toInt() * receipts[receiptKey].productPrice[productKey].toInt()
                            )
                            selectedPerson.clear()

                            if (receiptKey == receipts.size - 1 && productKey == receipts[receiptKey].productPrice.size - 1) {
                                isLastProduct.value = true
                                showCustomToast(context, "정산이 완료되었습니다. 정산을 확인해주세요.")
                            } else {
                                calculatorViewModel.incrementProductKey()
                                if (productKey >= receipts[receiptKey].productPrice.size) {
                                    calculatorViewModel.setProductKey(0)
                                    calculatorViewModel.incrementReceiptKey()
                                }
                                total = formatNumberWithCommas(
                                    (receipts[receiptKey].productQuantity[productKey].toInt() * receipts[receiptKey].productPrice[productKey].toInt()).toString()
                                )
                            }
                        } else {
                            showToastIfNotShowing("최소 1명 이상을 선택해주세요.")
                        }
                    }
                })
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}