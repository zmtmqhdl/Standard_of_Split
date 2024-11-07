package com.example.standardofsplit.View.Components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun Receipt_Add_Dialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
) {
    var newproductname by remember { mutableStateOf("") }
    var newprice by remember { mutableStateOf("") }
    var newquantity by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color.White,
            modifier = Modifier
                .width(500.dp)
                .height(400.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(20.dp)
            ) {
                InputField("상품명", newproductname) { newproductname = it }
                InputField("단가", formatNumberWithCommas(newprice)) { input ->
                    val numericOnly = input.replace(",", "").filter { it.isDigit() }
                    newprice = numericOnly
                }
                InputField("수량", newquantity) { input ->
                    val numericOnly = input.filter { it.isDigit() }
                    newquantity = numericOnly
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 35.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Small_Button("취소", onClick = onDismiss)
                    Spacer(modifier = Modifier.width(8.dp))
                    Small_Button("확인", onClick = { onConfirm(newproductname, newprice, newquantity) })
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
private fun formatNumberWithCommas(number: String): String {
    if (number.isEmpty()) return ""
    return try {
        val longNumber = number.toLong()
        String.format("%,d", longNumber)
    } catch (e: NumberFormatException) {
        number
    }
}

@Composable
fun Receipt_Name_Dialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    name: String
) {
    var newName by remember { mutableStateOf(name) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color.White,
            modifier = Modifier
                .width(500.dp)
                .height(400.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(20.dp)
            ) {
                InputField("영수증 이름", newName) { newName = it }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 35.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Small_Button("취소", onClick = onDismiss)
                    Spacer(modifier = Modifier.width(8.dp))
                    Small_Button("확인", onClick = { onConfirm(newName) })
                }
            }
        }
    }
}

@Composable
fun Receipt_Change_Dialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
    productName: String,
    price: String,
    quantity: String,
) {
    var newproductname by remember { mutableStateOf(productName) }
    var newprice by remember { mutableStateOf(price) }
    var newquantity by remember { mutableStateOf(quantity) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color.White,
            modifier = Modifier
                .width(500.dp)
                .height(400.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(20.dp)
            ) {
                InputField("상품명", newproductname) { newproductname = it }
                InputField("단가", formatNumberWithCommas(newprice)) { input ->
                    val numericOnly = input.replace(",", "").filter { it.isDigit() }
                    newprice = numericOnly
                }
                InputField("수량", newquantity) { input ->
                    val numericOnly = input.filter { it.isDigit() }
                    newquantity = numericOnly
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 35.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Small_Button("취소", onClick = onDismiss)
                    Spacer(modifier = Modifier.width(8.dp))
                    Small_Button("확인", onClick = { onConfirm(newproductname, newprice, newquantity) })
                }
            }
        }
    }
}

@Composable
fun Button_Name_Dialog(
    onDismiss: () -> Unit,
    onConfirm: (Int, String) -> Unit,
    name: String,
    index: Int
) {
    var newName by remember { mutableStateOf(name) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color.White,
            modifier = Modifier
                .width(500.dp)
                .height(400.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(20.dp)
            ) {
                InputField("버튼 이름", newName) { newName = it }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 35.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Small_Button("취소", onClick = onDismiss)
                    Spacer(modifier = Modifier.width(8.dp))
                    Small_Button("확인", onClick = { onConfirm(index, newName) })
                }
            }
        }
    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Text(text = label, fontWeight = FontWeight.Bold)
    TextField(
        value = value,
        onValueChange = onValueChange,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .height(50.dp)
            .fillMaxHeight(),
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        textStyle = TextStyle(fontSize = 16.sp)
    )
    Spacer(modifier = Modifier.padding(0.5.dp))
}