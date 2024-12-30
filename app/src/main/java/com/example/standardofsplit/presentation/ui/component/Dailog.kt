package com.example.standardofsplit.presentation.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.standardofsplit.presentation.ui.theme.Color
import com.example.standardofsplit.presentation.ui.theme.Shape
import com.example.standardofsplit.presentation.ui.theme.Typography

@Composable
fun InputField(
    text: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Text(
        text = text,
        style = Typography.inputFieldHeadTextStyle
    )
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .height(60.dp)
            .fillMaxHeight()
            .padding(bottom = 0.5.dp),
        shape = Shape.RoundedCRectangle,
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Gray2,
            focusedIndicatorColor = Color.Gray2
        ),
        textStyle = Typography.inputFieldTextStyle
    )
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
private fun DialogContainer(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.wrapContentSize(),
            shape = Shape.RoundedCRectangle,
            color = Color.Gray2,
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                content()
            }
        }
    }
}

@Composable
private fun DialogButtons(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(modifier = Modifier.weight(1f)) {
            if (onDelete != null) {
                DialogDeleteButton(
                    text = "삭제",
                    onClick = onDelete,
                )
            }
        }
        Row {
            DialogButton(
                text = "취소",
                onClick = onDismiss
            )
            Spacer(modifier = Modifier.width(8.dp))
            DialogButton(
                text = "확인",
                onClick = onConfirm
            )
        }
    }
}

@Composable
fun ReceiptAddDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    toastMessage: (String) -> Unit
) {
    var newName by remember { mutableStateOf("") }
    
    DialogContainer(onDismiss = onDismiss) {
        InputField(text = "신규 영수증 이름", value = newName, onValueChange = { newName = it })
        DialogButtons(
            onConfirm = {
                if (newName.isNotEmpty()) {
                    onConfirm(newName)
                } else {
                    toastMessage("영수증 이름을 작성해주세요.")
                }
            },
            onDismiss = onDismiss,
        )
    }
}

@Composable
fun ReceiptNameUpdateDialog(
    name: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    toastMessage: (String) -> Unit,
) {
    var newName by remember { mutableStateOf(name) }

    DialogContainer(onDismiss = onDismiss) {
        InputField("영수증 변경", newName) { newName = it }
        DialogButtons(
            onDismiss = onDismiss,
            onConfirm = {
                if (newName.isNotBlank()) {
                    onConfirm(newName)
                } else {
                    toastMessage("영수증 이름을 작성해주세요.")
                }
            },
            onDelete = onDelete
        )
    }
}

@Composable
fun ProductAddDialog(
    onConfirm: (String, Int, Int) -> Unit,
    onDismiss: () -> Unit,
    toastMessage: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }

    DialogContainer(onDismiss = onDismiss) {
        InputField("상품명", name) { name = it }
        InputField("단가", formatNumberWithCommas(price)) { input ->
            price = input.replace(",", "").filter { it.isDigit() }
        }
        InputField("수량", quantity) { input ->
            quantity = input.filter { it.isDigit() }
        }
        DialogButtons(
            onDismiss = onDismiss,
            onConfirm = {
                if (name.isNotEmpty() && price.isNotEmpty() && quantity.isNotEmpty()) {
                    val priceInt = price.toInt()
                    val quantityInt = quantity.toInt()
                    onConfirm(name, priceInt, quantityInt)
                } else {
                    toastMessage("모든 항목을 작성해주세요.")
                }
            }
        )
    }
}

@Composable
fun ProductUpdateDialog(
    onConfirm: (String, Int, Int) -> Unit,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    productName: String,
    price: Int,
    quantity: Int,
) {
    var newProductName by remember { mutableStateOf(productName) }
    var newPrice by remember { mutableIntStateOf(price) }
    var newQuantity by remember { mutableIntStateOf(quantity) }
    val context = LocalContext.current
    DialogContainer(onDismiss = onDismiss) {
        InputField("상품명", newProductName) { newProductName = it }
        InputField("단가", formatNumberWithCommas(newPrice.toString())) { input ->
            newPrice = input.replace(",", "").filter { it.isDigit() }.toIntOrNull() ?: 0
        }
        InputField("수량", newQuantity.toString()) { input ->
            newQuantity = input.filter { it.isDigit() }.toIntOrNull() ?: 0
        }
        DialogButtons(
            onDismiss = onDismiss,
            onConfirm = {
                if (newProductName.isNotEmpty() && newPrice > 0 && newQuantity > 0) {
                    onConfirm(newProductName, newPrice, newQuantity)
                } else {
                    showCustomToast(
                        context = context,
                        message = "모든 항목을 작성해주세요."
                    )
                }
            },
            onDelete = onDelete
        )
    }
}

@Composable
fun ButtonNameChangeDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    name: String,
) {
    var newName by remember { mutableStateOf(name) }
    DialogContainer(onDismiss = onDismiss) {
        InputField("버튼 이름", newName) { newName = it }
        DialogButtons(
            onConfirm = { onConfirm(newName) },
            onDismiss = onDismiss
        )
    }
}

@Composable
fun ResetDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = Shape.RoundedCRectangle,
            color = Color.Gray2,
            modifier = Modifier.wrapContentSize()
        ) {
            Column(
                modifier = Modifier.width(280.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "정산을 다시 하시겠습니까?",
                    style = Typography.dialogTitle1Style,
                    modifier = Modifier.padding(top = 15.dp)
                )
                Text(
                    text = "정산 내역이 초기화됩니다.",
                    style = Typography.dialogTitle2Style,
                    modifier = Modifier.padding(bottom = 15.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .drawBehind {
                                drawLine(
                                    color = Color.White,
                                    start = Offset(size.width, 0f),
                                    end = Offset(size.width, size.height),
                                    strokeWidth = 7f
                                )
                                drawLine(
                                    color = Color.White,
                                    start = Offset(0f, 0f),
                                    end = Offset(size.width, 0f),
                                    strokeWidth = 7f
                                )
                            },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray2
                        ),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        Text("아니요", color = Color.White)
                    }
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .drawBehind {
                                drawLine(
                                    color = Color.White,
                                    start = Offset(0f, 0f),
                                    end = Offset(0f, size.height),
                                    strokeWidth = 7f
                                )
                                drawLine(
                                    color = Color.White,
                                    start = Offset(0f, 0f),
                                    end = Offset(size.width, 0f),
                                    strokeWidth = 7f
                                )
                            },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray2
                        ),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        Text("네", color = Color.White)
                    }
                }
            }
        }
    }
}


@Composable
fun DetailDialog(
    name: String,
    onDismiss: () -> Unit,
    receiptDetails: Map<String, Map<String, Int>>
) {
    val total = receiptDetails.values.sumOf { products -> 
        products.values.sum() 
    }
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = Shape.RoundedCRectangle,
            color = androidx.compose.ui.graphics.Color.DarkGray,
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = name,
                    style = Typography.resultDetailStyle,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .width(500.dp)
                        .background(Color.Gray2, shape = RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    val entries = receiptDetails.entries.toList()
                    entries.forEachIndexed { index, (placeName, products) ->
                        item {
                            Text(
                                text = placeName,
                                style = Typography.dialogBlackBigTextStyle,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            products.forEach { (productName, amount) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, bottom = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = productName,
                                        style = Typography.dialogBlackTextStyle
                                    )
                                    Text(
                                        text = "${formatNumberWithCommas(amount.toString())}원",
                                        style = Typography.dialogBlackTextStyle
                                    )
                                }
                            }
                            
                            if (index < entries.size - 1) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    thickness = 1.dp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "총합: ${formatNumberWithCommas(total.toString())}원",
                        style = Typography.calculatorButtonTextStyle
                    )
                }

                DialogButton(
                    text = "확인",
                    onClick = onDismiss,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .width(240.dp)
                        .height(60.dp),  // 높이 증가
                )
            }
        }
    }
}

@Composable
fun AccountDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var newName by remember { mutableStateOf("") }

    DialogContainer(onDismiss = onDismiss) {
        InputField(text = "계좌 번호", value = newName, onValueChange = { newName = it })
        DialogButtons(
            onConfirm = {
                if (newName.isEmpty()) {
                    onConfirm("계좌 입력")
                } else {
                    onConfirm(newName)
                }
            },
            onDismiss = onDismiss,
        )
    }
}