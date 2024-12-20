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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.standardofsplit.presentation.ui.component.ButtonNameChangeDialog
import com.example.standardofsplit.presentation.ui.component.CalculateButton
import com.example.standardofsplit.presentation.ui.component.FunctionButton
import com.example.standardofsplit.presentation.ui.component.NameChangeToggleButton
import com.example.standardofsplit.presentation.ui.component.PersonSelectButton
import com.example.standardofsplit.presentation.ui.component.formatNumberWithCommas
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

    val personCount by startViewModel.personCount.collectAsState(0)

    val receipts by receiptViewModel.receipts.collectAsState()

    val receiptKey by calculatorViewModel.receiptKey.collectAsState(0)
    val productKey by calculatorViewModel.productKey.collectAsState(0)
    val buttonNames by calculatorViewModel.buttonNames.collectAsState(mutableListOf())
    val buttonStates by calculatorViewModel.buttonStates.collectAsState(List(8) { false })
    val changeMode by calculatorViewModel.changeMode.collectAsState(false)
    val index by calculatorViewModel.index.collectAsState(0)

    val context = LocalContext.current
    val showButtonNameChangeDialog = remember { mutableStateOf(false) }

    val total by remember {
        mutableStateOf(
            formatNumberWithCommas((receipts[receiptKey].productQuantity.value[productKey] * receipts[receiptKey].productPrice.value[productKey]).toString())
        )
    }

    LaunchedEffect(Unit) {
        calculatorViewModel.initializeTotalPay()
        calculatorViewModel.initializeButtonNames(personCount = personCount)
    }

    BackHandler { onBack() }

    if (showButtonNameChangeDialog.value) {
        val currentName = buttonNames[index]
        ButtonNameChangeDialog(
            onConfirm = { newName ->
                calculatorViewModel.updateButtonNames(
                    index = index, newName = newName
                )
            },
            onDismiss = { calculatorViewModel.closeButtonNameChangeDialog() },
            name = currentName,
        )
    }

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
                Text(
                    text = receipts[receiptKey].productName.toString(),
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
                PersonSelectButton(text = buttonNames[0], state = buttonStates[0], onClick = {
                    calculatorViewModel.personSelect(
                        receipts = receiptViewModel.receipts.value, index = 0, context = context
                    )
                })

                PersonSelectButton(text = buttonNames[1], state = buttonStates[1], onClick = {
                    calculatorViewModel.personSelect(
                        receipts = receiptViewModel.receipts.value, index = 1, context = context
                    )
                })
                Box(
                    modifier = Modifier
                        .width(216.dp)
                        .height(105.dp),
                    contentAlignment = Alignment.Center
                ) {
                    NameChangeToggleButton(
                        text1 = "OFF", text2 = "ON", onClick = {}, changeMode = changeMode
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
                PersonSelectButton(text = buttonNames[2], state = buttonStates[2], onClick = {
                    calculatorViewModel.personSelect(
                        receipts = receiptViewModel.receipts.value, index = 2, context = context
                    )
                })
                PersonSelectButton(text = buttonNames[3], state = buttonStates[3], onClick = {
                    calculatorViewModel.personSelect(
                        receipts = receiptViewModel.receipts.value, index = 3, context = context
                    )
                })
                Box(
                    modifier = Modifier
                        .width(216.dp)
                        .height(105.dp),
                    contentAlignment = Alignment.Center
                ) {
                    FunctionButton(text = "되돌리기", onClick = { calculatorViewModel.rollback() })
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PersonSelectButton(text = buttonNames[4], state = buttonStates[4], onClick = {
                    calculatorViewModel.personSelect(
                        receipts = receiptViewModel.receipts.value, index = 4, context = context
                    )
                })
                PersonSelectButton(text = buttonNames[5], state = buttonStates[5], onClick = {
                    calculatorViewModel.personSelect(
                        receipts = receiptViewModel.receipts.value, index = 5, context = context
                    )
                })
                Box(
                    modifier = Modifier
                        .width(216.dp)
                        .height(105.dp),
                    contentAlignment = Alignment.Center
                ) {
                    FunctionButton(text = "전체 선택", onClick = {
                        calculatorViewModel.endCheck(
                            receipts = receiptViewModel.receipts.value, context = context
                        )
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
                PersonSelectButton(text = buttonNames[6], state = buttonStates[6], onClick = {
                    calculatorViewModel.personSelect(
                        receipts = receiptViewModel.receipts.value, index = 6, context = context
                    )
                })
                PersonSelectButton(text = buttonNames[7], state = buttonStates[7], onClick = {
                    calculatorViewModel.personSelect(
                        receipts = receiptViewModel.receipts.value, index = 7, context = context
                    )
                })

                CalculateButton(
                    onClick = {
                        calculatorViewModel.calculate(
                            receipts = receipts, onNext = onNext, context = context
                        )
                    }, check = changeMode
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}