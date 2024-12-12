package com.example.standardofsplit.presentation.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.standardofsplit.presentation.ui.component.SubmitButton
import com.example.standardofsplit.presentation.ui.component.Receipt_Detail_Dialog
import com.example.standardofsplit.presentation.ui.component.Reset_Confirm_Dialog
import com.example.standardofsplit.presentation.viewModel.CalculatorViewModel
import com.example.standardofsplit.presentation.viewModel.StartViewModel
import java.lang.String.format

@Composable
fun ResultScreen (
    onBack: () -> Unit
) {
//
//    var showResetDialog by remember { mutableStateOf(false) }
//    var showDetailDialog by remember { mutableStateOf(false) }
//    var selectedPerson by remember { mutableStateOf<Pair<String, Map<String, Map<String, Int>>>?>(null) }
//
//    LaunchedEffect(Unit) {
//        calculatorViewModel.updateTotalPay()
//    }
//
//    BackHandler {
//        showResetDialog = true
//    }
//
//    val personCount by startViewModel.personCount.collectAsState(2)
//    val personPayMap by calculatorViewModel.personPay.observeAsState(mutableMapOf())
//    val buttonNames by calculatorViewModel.buttonNames.observeAsState(emptyMap())
//
//    Log.d("로그", personPayMap.values.toString())
//    val personTotals = (1..personCount).map { personIndex ->
//        val personData = personPayMap[personIndex] ?: mutableMapOf()
//        val total = personData.values.sumOf { products ->
//            products.values.sum()
//        }
//        Pair(buttonNames[personIndex.toString()] ?: "인원 $personIndex", total)
//    }
//
//    Box(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(760.dp)
//                    .padding(top = 10.dp)
//            ) {
//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.SpaceEvenly
//                ) {
//                    for (i in 0..3) {
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.SpaceEvenly
//                        ) {
//                            val leftIndex = 2 * i
//                            ResultCard(
//                                name = if (leftIndex < personCount) personTotals[leftIndex].first else "",
//                                amount = if (leftIndex < personCount) personTotals[leftIndex].second else 0,
//                                isActive = leftIndex < personCount,
//                                onClick = {
//                                    if (leftIndex < personCount) {
//                                        selectedPerson = Pair(
//                                            personTotals[leftIndex].first,
//                                            personPayMap[leftIndex + 1] ?: emptyMap()
//                                        )
//                                        showDetailDialog = true
//                                    }
//                                }
//                            )
//
//                            val rightIndex = 2 * i + 1
//                            ResultCard(
//                                name = if (rightIndex < personCount) personTotals[rightIndex].first else "",
//                                amount = if (rightIndex < personCount) personTotals[rightIndex].second else 0,
//                                isActive = rightIndex < personCount,
//                                onClick = {
//                                    if (rightIndex < personCount) {
//                                        selectedPerson = Pair(
//                                            personTotals[rightIndex].first,
//                                            personPayMap[rightIndex + 1] ?: emptyMap()
//                                        )
//                                        showDetailDialog = true
//                                    }
//                                }
//                            )
//                        }
//                    }
//                }
//            }
//        }
//
//        Box(
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .fillMaxWidth()
//                .padding(bottom = 50.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            SubmitButton(
//                text = "공유하기",
//                onClick = { /* 나중에 기능 추가 */ }
//            )
//        }
//    }
//
//    if (showResetDialog) {
//        Reset_Confirm_Dialog(
//            onDismiss = { showResetDialog = false },
//            onConfirm = {
//                calculatorViewModel.resetTotalPay()
//                showResetDialog = false
//                onBack()
//            }
//        )
//    }
//
//    if (showDetailDialog && selectedPerson != null) {
//        Receipt_Detail_Dialog(
//            onDismiss = { showDetailDialog = false },
//            name = selectedPerson!!.first,
//            receiptDetails = selectedPerson!!.second
//        )
//    }
//}
//
//@Composable
//private fun ResultCard(
//    name: String,
//    amount: Int,
//    isActive: Boolean,
//    onClick: () -> Unit
//) {
//    Box(
//        modifier = Modifier
//            .size(width = 180.dp, height = 160.dp)
//            .clip(RoundedCornerShape(10.dp))
//            .background(if (isActive) Color.DarkGray else Color.Black)
//            .clickable(
//                enabled = isActive,
//                onClick = onClick
//            )
//    ) {
//        if (isActive) {
//            Column(
//                modifier = Modifier.fillMaxSize(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.SpaceEvenly
//            ) {
//                Text(
//                    text = name,
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.Yellow
//                )
//                Text(
//                    text = "${formatNumberWithCommas(amount)}원",
//                    fontSize = 24.sp,
//                    color = Color.White
//                )
//                Spacer(modifier = Modifier.height(24.dp))
//            }
//        }
//    }
}
