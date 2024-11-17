package com.example.standardofsplit.View.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standardofsplit.View.Components.Basic_Button
import com.example.standardofsplit.ViewModel.Calculator
import com.example.standardofsplit.ViewModel.Start
import com.example.standardofsplit.ui.theme.DarkGray
import com.example.standardofsplit.ui.theme.White
import com.example.standardofsplit.ui.theme.Yellow

@Composable
fun ResultScreen(
    start: Start,
    calculator: Calculator
) {
    val personCount by start.personCount.observeAsState(2)
    val personPayMap by calculator.personPay.observeAsState(initial = mutableMapOf())
    
    val personTotals = (1..personCount).map { personIndex ->
        val personData = personPayMap[personIndex] ?: mutableMapOf()
        val total = personData.values.sumOf { products -> 
            products.values.sum() 
        }
        Pair("인원 $personIndex", total)
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
                    for (i in 0 until (personCount + 1) / 2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            if (2 * i < personCount) {
                                ResultCard(
                                    name = personTotals[2 * i].first,
                                    amount = personTotals[2 * i].second,
                                    onClick = { /* 나중에 기능 추가 */ }
                                )
                            }
                            if (2 * i + 1 < personCount) {
                                ResultCard(
                                    name = personTotals[2 * i + 1].first,
                                    amount = personTotals[2 * i + 1].second,
                                    onClick = { /* 나중에 기능 추가 */ }
                                )
                            }
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
                content = "완료",
                onClick = { /* 나중에 기능 추가 */ }
            )
        }
    }
}

@Composable
private fun ResultCard(
    name: String,
    amount: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 180.dp, height = 160.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(DarkGray)
            .clickable(onClick = onClick)
    ) {
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

private fun formatNumberWithCommas(number: Int): String {
    return String.format("%,d", number)
}
