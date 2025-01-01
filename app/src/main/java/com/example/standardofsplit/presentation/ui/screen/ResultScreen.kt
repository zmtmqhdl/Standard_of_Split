package com.example.standardofsplit.presentation.ui.screen

import android.view.View
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.standardofsplit.presentation.ui.component.AccountDialog
import com.example.standardofsplit.presentation.ui.component.DetailDialog
import com.example.standardofsplit.presentation.ui.component.ResetDialog
import com.example.standardofsplit.presentation.ui.component.SubmitButton
import com.example.standardofsplit.presentation.ui.theme.Typography
import com.example.standardofsplit.presentation.viewModel.CalculatorViewModel
import com.example.standardofsplit.presentation.viewModel.ResultViewModel
import com.example.standardofsplit.presentation.viewModel.StartViewModel

@Composable
private fun ResultCard(
    name: String, amount: Int, isActive: Boolean, onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 140.dp, height = 120.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (isActive) Color.DarkGray else Color.Black)
            .clickable(
                enabled = isActive, onClick = onClick
            )
    ) {
        if (isActive) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = name,
                    style = Typography.resultCardStyle
                )
                Text(
                    text = "${formatNumberWithCommas(amount)}원",
                    style = Typography.resultCardPayStyle
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun ResultScreen(
    startViewModel: StartViewModel, calculatorViewModel: CalculatorViewModel, onBack: () -> Unit
) {
    val context = LocalContext.current
    val rootView = (context as android.app.Activity).window.decorView.findViewById<View>(android.R.id.content)

    val resultViewModel: ResultViewModel = hiltViewModel()

    val personCount by startViewModel.personCount.collectAsState()

    val totalPay by calculatorViewModel.totalPay.collectAsState()
    val buttonNames by calculatorViewModel.buttonNames.collectAsState()

    val showDetailDialog by resultViewModel.showDetailDialog.collectAsState()
    val showResetDialog by resultViewModel.showResetDialog.collectAsState()
    val showAccountDialog by resultViewModel.showAccountDialog.collectAsState()

    val accountText by resultViewModel.accountText.collectAsState()

    var selectedPerson by remember {
        mutableStateOf<Pair<String, Map<String, Map<String, Int>>>?>(null)
    }

    val personTotals = (0..7).map { personIndex ->
        val totalPayData = totalPay.payment.value[personIndex] ?: emptyMap()
        val pay = totalPayData.values.sumOf { products -> products.values.sum() }
        Pair(buttonNames[personIndex], pay)
    }

    if (showResetDialog) {
        ResetDialog(onDismiss = { resultViewModel.changeResetDialog() }, onConfirm = {
            calculatorViewModel.initializeTotalPay()
            resultViewModel.changeResetDialog()
            onBack()
        })
    }

    if (showDetailDialog && selectedPerson != null) {
        DetailDialog(
            onDismiss = { resultViewModel.changeDetailDialog() },
            name = selectedPerson!!.first,
            receiptDetails = selectedPerson!!.second
        )
    }

    if (showAccountDialog) {
        AccountDialog(
            onConfirm = { newName ->
                resultViewModel.accountTextUpdate(newName = newName)
                resultViewModel.changeAccountDialog()
            },
            onDismiss = { resultViewModel.changeAccountDialog() },
        )
    }

    BackHandler {
        resultViewModel.changeResetDialog()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(760.dp)
                    .padding(top = 10.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (i in 0..3) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            val leftIndex = 2 * i
                            ResultCard(name = if (leftIndex < personCount) personTotals[leftIndex].first else "",
                                amount = if (leftIndex < personCount) personTotals[leftIndex].second else 0,
                                isActive = leftIndex < personCount,
                                onClick = {
                                    if (leftIndex < personCount) {
                                        selectedPerson = Pair(
                                            personTotals[leftIndex].first,
                                            totalPay.payment.value[leftIndex] ?: emptyMap()
                                        )
                                        resultViewModel.changeDetailDialog()
                                    }
                                }
                            )

                            val rightIndex = 2 * i + 1
                            ResultCard(name = if (rightIndex < personCount) personTotals[rightIndex].first else "",
                                amount = if (rightIndex < personCount) personTotals[rightIndex].second else 0,
                                isActive = rightIndex < personCount,
                                onClick = {
                                    if (rightIndex < personCount) {
                                        selectedPerson = Pair(
                                            personTotals[rightIndex].first,
                                            totalPay.payment.value[rightIndex] ?: emptyMap()
                                        )
                                        resultViewModel.changeDetailDialog()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 50.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = accountText,
                    style = resultViewModel.changeAccountTextStyle(),
                    modifier = Modifier.clickable {
                        resultViewModel.changeAccountDialog()
                    }.padding(bottom = 25.dp),
                )
                SubmitButton(text = "저장 & 공유", onClick = {
                    resultViewModel.capture(context = context, rootView = rootView)
                })
            }
        }
    }
}
