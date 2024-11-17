package com.example.standardofsplit.View.Screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.standardofsplit.View.Components.Basic_Button
import com.example.standardofsplit.View.Components.Reset_Confirm_Dialog
import com.example.standardofsplit.ViewModel.Calculator
import com.example.standardofsplit.ViewModel.Start
import com.example.standardofsplit.ui.theme.DarkGray
import com.example.standardofsplit.ui.theme.White
import com.example.standardofsplit.ui.theme.Yellow

@Composable
fun ResultScreen(
    start: Start,
    calculator: Calculator,
    onBack: () -> Unit
) {
    var showResetDialog by remember { mutableStateOf(false) }
    
    BackHandler {
        showResetDialog = true
    }

    val personCount by start.personCount.observeAsState(2)
    val personPayMap by calculator.personPay.observeAsState(mutableMapOf())
    val buttonNames by calculator.buttonNames.observeAsState(emptyMap())
    
    val personTotals = (1..personCount).map { personIndex ->
        val personData = personPayMap[personIndex] ?: mutableMapOf()
        val total = personData.values.sumOf { products -> 
            products.values.sum() 
        }
        Pair(buttonNames[personIndex.toString()] ?: "인원 $personIndex", total)
    }
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(760.dp)
                    .padding(top = 15.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (i in 0..3) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            val leftIndex = 2 * i
                            ResultCard(
                                name = if (leftIndex < personCount) personTotals[leftIndex].first else "",
                                amount = if (leftIndex < personCount) personTotals[leftIndex].second else 0,
                                isActive = leftIndex < personCount,
                                onClick = { /* 나중에 기능 추가 */ }
                            )
                            
                            val rightIndex = 2 * i + 1
                            ResultCard(
                                name = if (rightIndex < personCount) personTotals[rightIndex].first else "",
                                amount = if (rightIndex < personCount) personTotals[rightIndex].second else 0,
                                isActive = rightIndex < personCount,
                                onClick = { /* 나중에 기능 추가 */ }
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
            Basic_Button(
                content = "공유하기",
                onClick = { /* 나중에 기능 추가 */ }
            )
        }
    }

    if (showResetDialog) {
        Reset_Confirm_Dialog(
            onDismiss = { showResetDialog = false },
            onConfirm = {
                calculator.resetPersonPay()
                onBack()
            }
        )
    }
}

@Composable
private fun ResultCard(
    name: String,
    amount: Int,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 180.dp, height = 160.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (isActive) DarkGray else Color.Black)
            .clickable(
                enabled = isActive,
                onClick = onClick
            )
    ) {
        if (isActive) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Yellow
                )
                Text(
                    text = "${formatNumberWithCommas(amount)}원",
                    fontSize = 20.sp,
                    color = White,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

private fun formatNumberWithCommas(number: Int): String {
    return String.format("%,d", number)
}
