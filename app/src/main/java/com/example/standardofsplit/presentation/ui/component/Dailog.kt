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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
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
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween  // 변경
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
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    toastMessage: (String) -> Unit
) {
    var newName by remember { mutableStateOf("") }
    
    DialogContainer(onDismiss = onDismiss) {
        InputField(text = "신규 영수증 이름", value = newName, onValueChange = { newName = it })
        DialogButtons(
            onDismiss = onDismiss,
            onConfirm = {
                if (newName.isNotEmpty()) {
                    onConfirm(newName)
                } else {
                    toastMessage("영수증 이름을 작성해주세요.")
                }
            }
        )
    }
}

@Composable
fun ReceiptNameUpdateDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    onDelete: () -> Unit,
    toastMessage: (String) -> Unit,
    name: String
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
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
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
                    onConfirm(name, price, quantity)
                } else {
                    toastMessage("모든 항목을 작성해주세요.")
                }
            }
        )
    }
}

// Preview

@Preview
@Composable
fun Preview_InputField() {
    var newName by remember { mutableStateOf("") }

    InputField(text = "테스트", value = newName, onValueChange = { newName = it})
}

@Preview
@Composable
fun Preview_DialogContainer() {
    DialogContainer(
        onDismiss = { },
        content = { }
    )
}

@Preview
@Composable
fun Preview_DialogButtons() {
    DialogButtons(
        onDismiss = { },
        onConfirm = { },
        onDelete = { }
    )
}











@Composable
fun Reset_Confirm_Dialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = Shape.RoundedCRectangle,
            color = Color.White,
            modifier = Modifier.wrapContentSize()
        ) {
            Column(
                modifier = Modifier.width(280.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "정산을 다시 하시겠습니까?",
                    color = Color.White,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 15.dp)
                )
                Text(
                    text = "정산 내역이 초기화됩니다.",
                    color = Color.White,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
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
                            containerColor = Color.White
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
                            containerColor = Color.White
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
fun ProductUpdateDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
    onDelete: () -> Unit,
    productName: String,
    price: String,
    quantity: String,
) {
    var newproductname by remember { mutableStateOf(productName) }
    var newprice by remember { mutableStateOf(price) }
    var newquantity by remember { mutableStateOf(quantity) }
    val context = LocalContext.current
    var isToastShowing by remember { mutableStateOf(false) }

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
            onConfirm = {
                if (newproductname.isNotEmpty() && newprice.isNotEmpty() && newquantity.isNotEmpty()) {
                    onConfirm(newproductname, newprice, newquantity)
                } else if (!isToastShowing) {
                    isToastShowing = true
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

@Composable
fun Receipt_Detail_Dialog(
    onDismiss: () -> Unit,
    name: String,
    receiptDetails: Map<String, Map<String, Int>>
) {
    val total = receiptDetails.values.sumOf { products -> 
        products.values.sum() 
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = Shape.RoundedCRectangle,
            color = Color.White,
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
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Yellow,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .width(500.dp)
                        .background(Color.White, shape = RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    val entries = receiptDetails.entries.toList()
                    entries.forEachIndexed { index, (placeName, products) ->
                        item {
                            Text(
                                text = placeName,
                                fontSize = 24.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
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
                                        fontSize = 16.sp,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "${formatNumberWithCommas(amount.toString())}원",
                                        fontSize = 16.sp,
                                        color = Color.White
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
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
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