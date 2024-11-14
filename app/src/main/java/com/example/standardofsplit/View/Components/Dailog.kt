package com.example.standardofsplit.View.Components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

private object DialogDefaults {
    val shape = RoundedCornerShape(10.dp)
    val backgroundColor = Color.White
    val dialogSize = Modifier
        .width(500.dp)
        .height(400.dp)
    val contentPadding = 20.dp
    val buttonSpacing = 8.dp
    val topPadding = 35.dp
}

@Composable
private fun getTextFieldColors() = TextFieldDefaults.colors(
    unfocusedIndicatorColor = Color.Transparent,
    focusedIndicatorColor = Color.Transparent
)

@Composable
private fun DialogContainer(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = DialogDefaults.shape,
            color = DialogDefaults.backgroundColor,
            modifier = DialogDefaults.dialogSize
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(DialogDefaults.contentPadding)
            ) {
                content()
            }
        }
    }
}

@Composable
private fun DialogButtons(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = DialogDefaults.topPadding),
        horizontalArrangement = Arrangement.End
    ) {
        Small_Button("취소", onClick = onDismiss)
        Spacer(modifier = Modifier.width(DialogDefaults.buttonSpacing))
        Small_Button("확인", onClick = onConfirm)
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
        shape = DialogDefaults.shape,
        modifier = Modifier
            .height(50.dp)
            .fillMaxHeight(),
        colors = getTextFieldColors(),
        textStyle = TextStyle(fontSize = 16.sp)
    )
    Spacer(modifier = Modifier.padding(0.5.dp))
}

@SuppressLint("DefaultLocale")
fun formatNumberWithCommas(number: String): String {
    if (number.isEmpty()) return ""
    return try {
        val longNumber = number.toLong()
        String.format("%,d", longNumber)
    } catch (e: NumberFormatException) {
        number
    }
}

@Composable
fun Receipt_Add_Dialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
) {
    var newproductname by remember { mutableStateOf("") }
    var newprice by remember { mutableStateOf("") }
    var newquantity by remember { mutableStateOf("") }

    DialogContainer(onDismiss = onDismiss) {
        InputField("상품명", newproductname) { newproductname = it }
        InputField("단가", formatNumberWithCommas(newprice)) { input ->
            newprice = input.replace(",", "").filter { it.isDigit() }
        }
        InputField("수량", newquantity) { input ->
            newquantity = input.filter { it.isDigit() }
        }
        DialogButtons(
            onDismiss = onDismiss,
            onConfirm = { onConfirm(newproductname, newprice, newquantity) }
        )
    }
}

@Composable
fun Receipt_Name_Dialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    name: String
) {
    var newName by remember { mutableStateOf(name) }

    DialogContainer(onDismiss = onDismiss) {
        InputField("영수증 이름", newName) { newName = it }
        DialogButtons(
            onDismiss = onDismiss,
            onConfirm = { onConfirm(newName) }
        )
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

    DialogContainer(onDismiss = onDismiss) {
        InputField("상품명", newproductname) { newproductname = it }
        InputField("단가", formatNumberWithCommas(newprice)) { input ->
            newprice = input.replace(",", "").filter { it.isDigit() }
        }
        InputField("수량", newquantity) { input ->
            newquantity = input.filter { it.isDigit() }
        }
        DialogButtons(
            onDismiss = onDismiss,
            onConfirm = { onConfirm(newproductname, newprice, newquantity) }
        )
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

    DialogContainer(onDismiss = onDismiss) {
        InputField("버튼 이름", newName) { newName = it }
        DialogButtons(
            onDismiss = onDismiss,
            onConfirm = { onConfirm(index, newName) }
        )
    }
}